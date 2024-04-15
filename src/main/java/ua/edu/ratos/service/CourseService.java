package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Course;
import ua.edu.ratos.dao.entity.lms.LMS;
import ua.edu.ratos.dao.entity.lms.LMSCourse;
import ua.edu.ratos.dao.repository.CourseRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.CourseInDto;
import ua.edu.ratos.service.dto.in.LMSCourseInDto;
import ua.edu.ratos.service.dto.out.CourseMinOutDto;
import ua.edu.ratos.service.dto.out.CourseOutDto;
import ua.edu.ratos.service.transformer.mapper.CourseMapper;
import ua.edu.ratos.service.transformer.mapper.CourseMinMapper;
import ua.edu.ratos.service.transformer.CourseTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {

    private static final String COURSE_NOT_FOUND = "Requested Course not found, courseId = ";

    @PersistenceContext
    private final EntityManager em;

    private final CourseRepository courseRepository;

    private final CourseTransformer courseTransformer;

    private final CourseMapper courseMapper;

    private final CourseMinMapper courseMinMapper;

    private final AccessChecker accessChecker;

    private final SecurityUtils securityUtils;


    @Transactional
    public CourseOutDto save(@NonNull final CourseInDto dto) {
        Course course = courseTransformer.toEntity(dto);
        Course result = courseRepository.save(course);
        return courseMapper.toDto(result);
    }

    @Transactional
    public CourseOutDto save(@NonNull final LMSCourseInDto dto) {
        Course course = courseTransformer.toLMSEntity(dto);
        Course result = courseRepository.save(course);
        return courseMapper.toDto(result);
    }

    @Transactional
    public CourseOutDto update(@NonNull final CourseInDto dto) {
        if (dto.getCourseId()==null)
            throw new RuntimeException("Failed to update, courseId is nullable!");
        Course course = checkModificationPossibility(dto.getCourseId());
        course = courseTransformer.toEntity(course, dto);
        Course result = courseRepository.save(course);
        return courseMapper.toDto(result);
    }

    @Transactional
    public CourseOutDto update(@NonNull final LMSCourseInDto dto) {
        if (dto.getCourseId()==null)
            throw new RuntimeException("Failed to update LMS course, courseId is nullable!");
        Course course = checkModificationPossibility(dto.getCourseId());
        course = courseTransformer.toLMSEntity(course, dto);
        Course result = courseRepository.save(course);
        return courseMapper.toDto(result);
    }

    @Transactional
    public void updateName(@NonNull final Long courseId, @NonNull final String name) {
        Course course = checkModificationPossibility(courseId);
        course.setName(name);
    }

    @Transactional
    public void associateWithLMS(@NonNull final Long courseId, @NonNull final Long lmsId) {
        Course course = checkModificationPossibility(courseId);
        LMSCourse lmsCourse = new LMSCourse();
        lmsCourse.setCourseId(courseId);
        lmsCourse.setCourse(course);
        lmsCourse.setLms(em.getReference(LMS.class, lmsId));
        course.setLmsCourse(lmsCourse);
    }

    @Transactional
    public void disassociateWithLMS(@NonNull final Long courseId) {
        Course course = checkModificationPossibility(courseId);
        LMSCourse lmsCourse = course.getLmsCourse();
        lmsCourse.setCourse(null);
        course.setLmsCourse(null);
    }

    @Transactional
    public void deleteById(@NonNull final Long courseId) {
        Course course = checkModificationPossibility(courseId);
        courseRepository.delete(course);
    }

    @Transactional
    public void deleteByIdSoft(@NonNull final Long courseId) {
        Course course = checkModificationPossibility(courseId);
        course.setDeleted(true);
    }

    private Course checkModificationPossibility(@NonNull final Long courseId) {
        Course course= courseRepository.findForSecurityById(courseId)
                .orElseThrow(()->new EntityNotFoundException(COURSE_NOT_FOUND + courseId));
        accessChecker.checkModifyAccess(course.getAccess(), course.getStaff());
        return course;
    }

    //-------------------------------------------------Staff (min for drop down)----------------------------------------
    @Transactional(readOnly = true)
    public Set<CourseMinOutDto> findAllForDropdownByStaffId() {
        return courseRepository.findAllForDropDownByStaffId(securityUtils.getAuthStaffId())
                .stream()
                .map(courseMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<CourseMinOutDto> findAllForDropdownByDepartmentId() {
        return courseRepository.findAllForDropDownByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(courseMinMapper::toDto)
                .collect(Collectors.toSet());
    }


    @Transactional(readOnly = true)
    public Set<CourseMinOutDto> findAllForDropdownByDepartmentId(@NonNull final Long depId) {
        return courseRepository.findAllForDropDownByDepartmentId(depId)
                .stream()
                .map(courseMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff (table)--------------------------------------------------
    @Transactional(readOnly = true)
    public Set<CourseOutDto> findAllForTableByDepartment() {
        return courseRepository.findAllForTableByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<CourseOutDto> findAllForTableByDepartmentId(@NonNull Long depId) {
        return courseRepository.findAllForTableByDepartmentId(depId)
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toSet());
    }


    //-----------------------------------------------Admin (for simple table)-------------------------------------------
    @Transactional(readOnly = true)
    public Page<CourseOutDto> findAll(@NonNull final Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }
}

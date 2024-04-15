package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Access;
import ua.edu.ratos.dao.entity.Course;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.lms.LMS;
import ua.edu.ratos.dao.entity.lms.LMSCourse;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.CourseInDto;
import ua.edu.ratos.service.dto.in.LMSCourseInDto;
import ua.edu.ratos.service.transformer.CourseTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
public class CourseTransformerImpl implements CourseTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final SecurityUtils securityUtils;

    @Override
    public Course toEntity(@NonNull final CourseInDto dto) {
        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setName(dto.getName());
        // If it is a new one!
        if (dto.getCourseId() == null)
            course.setCreated(OffsetDateTime.now());
        course.setAccess(em.getReference(Access.class, dto.getAccessId()));
        course.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        course.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return course;
    }

    @Override
    public Course toLMSEntity(@NonNull final LMSCourseInDto dto) {
        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setName(dto.getName());
        // If it is a new one!
        if (dto.getCourseId() == null)
            course.setCreated(OffsetDateTime.now());
        course.setAccess(em.getReference(Access.class, dto.getAccessId()));
        course.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        course.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        // Add LMS
        LMSCourse lmsCourse = new LMSCourse();
        lmsCourse.setCourseId(dto.getCourseId());
        lmsCourse.setCourse(course);
        lmsCourse.setLms(em.getReference(LMS.class, dto.getLmsId()));
        course.setLmsCourse(lmsCourse);
        return course;
    }
    //---------------------------------------------Mutator--------------------------------------------------------------

    @Override
    public Course toEntity(@NonNull final Course course, @NonNull final CourseInDto dto) {
        course.setCourseId(dto.getCourseId());
        course.setName(dto.getName());
        // If it is a new one!
        if (dto.getCourseId() == null)
            course.setCreated(OffsetDateTime.now());
        course.setAccess(em.getReference(Access.class, dto.getAccessId()));
        return course;
    }

    @Override
    public Course toLMSEntity(@NonNull final Course course, @NonNull final LMSCourseInDto dto) {
        course.setCourseId(dto.getCourseId());
        course.setName(dto.getName());
        // If it is a new one!
        if (dto.getCourseId() == null)
            course.setCreated(OffsetDateTime.now());
        course.setAccess(em.getReference(Access.class, dto.getAccessId()));
        // Add LMS
        LMSCourse lmsCourse = new LMSCourse();
        lmsCourse.setCourseId(dto.getCourseId());
        lmsCourse.setCourse(course);
        lmsCourse.setLms(em.getReference(LMS.class, dto.getLmsId()));
        course.setLmsCourse(lmsCourse);
        return course;
    }
}

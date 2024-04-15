package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.CourseService;
import ua.edu.ratos.service.dto.in.CourseInDto;
import ua.edu.ratos.service.dto.in.LMSCourseInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.CourseMinOutDto;
import ua.edu.ratos.service.dto.out.CourseOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/instructor/courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseOutDto> save(@Valid @RequestBody CourseInDto dto) {
        CourseOutDto courseOutDto = courseService.save(dto);
        log.debug("Saved Course, courseId = {}", courseOutDto.getCourseId());
        return ResponseEntity.ok().body(courseOutDto);
    }

    @PostMapping(value = "/instructor/lms-courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseOutDto> save(@Valid @RequestBody LMSCourseInDto dto) {
        CourseOutDto courseOutDto = courseService.save(dto);
        log.debug("Saved LMS Course, courseId = {}", courseOutDto.getCourseId());
        return ResponseEntity.ok().body(courseOutDto);
    }

    @PutMapping(value = "/instructor/courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CourseOutDto> update(@Valid @RequestBody CourseInDto dto) {
        CourseOutDto courseOutDto = courseService.update(dto);
        log.debug("Updated Course, courseId = {}", courseOutDto.getCourseId());
        return ResponseEntity.ok().body(courseOutDto);
    }

    @PutMapping(value = "/instructor/lms-courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CourseOutDto> update(@Valid @RequestBody LMSCourseInDto dto) {
        CourseOutDto courseOutDto = courseService.update(dto);
        log.debug("Updated LMS Course, courseId = {}", courseOutDto.getCourseId());
        return ResponseEntity.ok().body(courseOutDto);
    }

    @PatchMapping(value = "/instructor/courses/{courseId}/name", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long courseId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        courseService.updateName(courseId, name);
        log.debug("Updated Course's name, courseId = {}, new name = {}", courseId, name);
    }

    @PatchMapping("/instructor/courses/{courseId}/associate/{lmsId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void associateWithLMS(@PathVariable Long courseId, @PathVariable Long lmsId) {
        courseService.associateWithLMS(courseId, lmsId);
        log.debug("Associated Course with LMS, courseId = {}, lmsId = {}", courseId, lmsId);
    }

    @PatchMapping("/instructor/lms-courses/{courseId}/disassociate")
    @ResponseStatus(value = HttpStatus.OK)
    public void disassociateWithLMS(@PathVariable Long courseId) {
        courseService.disassociateWithLMS(courseId);
        log.debug("Disassociated Course with LMS, courseId = {}", courseId);
    }

    @DeleteMapping("/instructor/courses/{courseId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long courseId) {
        try {
            courseService.deleteById(courseId);
            log.debug("Deleted Course, courseId = {}", courseId);
        } catch (Exception e) {
            courseService.deleteByIdSoft(courseId);
            log.debug("Soft deleted Course, courseId = {}", courseId);
        }
    }

    //-------------------------------------------Staff min drop-down----------------------------------------------------

    @GetMapping(value="/department/courses-dropdown/all-courses-by-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<CourseMinOutDto> findAllForDropDownByStaffId() {
        return courseService.findAllForDropdownByStaffId();
    }

    @GetMapping(value="/department/courses-dropdown/all-courses-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<CourseMinOutDto> findAllForDropDownByDepartmentId() {
        return courseService.findAllForDropdownByDepartmentId();
    }

    @GetMapping(value="/fac-admin/courses-dropdown/all-courses-by-department", params = "depId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<CourseMinOutDto> findAllForDropDownByDepartmentId(@RequestParam final Long depId) {
        return courseService.findAllForDropdownByDepartmentId(depId);
    }

    //----------------------------------------------Staff table---------------------------------------------------------

    @GetMapping(value="/department/courses-table/all-courses-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<CourseOutDto> findAllForTableByDepartment() {
        return courseService.findAllForTableByDepartment();
    }

    @GetMapping(value="/fac-admin/courses-table/all-courses-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<CourseOutDto> findAllForTableByDepartmentId(@RequestParam Long depId) {
        return courseService.findAllForTableByDepartmentId(depId);
    }
}

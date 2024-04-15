package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ratos.dao.entity.Course;

import jakarta.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // For security purposes to check "modify"-access to a course
    @Query(value = "SELECT c FROM Course c join fetch c.access a join fetch c.staff s join fetch s.user join fetch s.department d where c.courseId =?1")
    Optional<Course> findForSecurityById(Long courseId);

    // -----------------------------------------------------Staff (drop down)-------------------------------------------
    @Query(value = "SELECT new Course(c.courseId, c.name) FROM Course c join c.staff s where s.staffId =?1")
    Set<Course> findAllForDropDownByStaffId(Long staffId);

    @Query(value = "SELECT new Course(c.courseId, c.name) FROM Course c join c.department d where d.depId =?1")
    Set<Course> findAllForDropDownByDepartmentId(Long depId);

    // -----------------------------------------------------Staff (table)-----------------------------------------------

    @Query(value = "SELECT c FROM Course c join fetch c.staff s join fetch s.user u join fetch s.position p join fetch c.access a left join fetch c.lmsCourse lc left join fetch lc.lms join c.department d where d.depId =?1")
    Set<Course> findAllForTableByDepartmentId(Long depId);

    //-----------------------------------------------------REPORT on content--------------------------------------------
    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(c) as count FROM Course c left join c.department d join d.faculty f join f.organisation o where d.depId=?1 group by d.depId")
    Tuple countCoursesByDepOfDepId(Long depId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(c) as count FROM Course c left join c.department d join d.faculty f join f.organisation o where f.facId=?1 group by d.depId")
    Set<Tuple> countCoursesByDepOfFacId(Long facId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(c) as count FROM Course c left join c.department d join d.faculty f join f.organisation o where o.orgId=?1 group by d.depId")
    Set<Tuple> countCoursesByDepOfOrgId(Long orgId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(c) as count FROM Course c left join c.department d join d.faculty f join f.organisation o group by d.depId")
    Set<Tuple> countCoursesByDepOfRatos();

    // -------------------------------------------------------ADMIN-TABLE ----------------------------------------------
    @Query(value = "SELECT c FROM Course c join fetch c.staff s join fetch s.user join fetch s.position join fetch c.access", countQuery = "SELECT count(c) FROM Course c")
    Page<Course> findAll(Pageable pageable);
}

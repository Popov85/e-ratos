package ua.edu.ratos.dao.repository.lms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ratos.dao.entity.lms.LMSCourse;

import jakarta.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

public interface LMSCourseRepository extends JpaRepository<LMSCourse, Long> {

    // For security purposes to check "modify"-access to a LMSCourse
    @Query(value = "SELECT c FROM LMSCourse c join fetch c.course co join fetch co.access a join fetch co.staff s join fetch s.user join fetch co.department d where c.courseId =?1")
    Optional<LMSCourse> findForSecurityById(Long courseId);

    //-------------------------------------------------REPORT on content------------------------------------------------
    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(lmsc) as count FROM LMSCourse lmsc left join lmsc.course c join c.department d join d.faculty f join f.organisation o where d.depId=?1 group by d.depId")
    Tuple countLMSCoursesByDepOfDepId(Long depId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(lmsc) as count FROM LMSCourse lmsc left join lmsc.course c join c.department d join d.faculty f join f.organisation o where f.facId=?1 group by d.depId")
    Set<Tuple> countLMSCoursesByDepOfFacId(Long facId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(lmsc) as count FROM LMSCourse lmsc left join lmsc.course c join c.department d join d.faculty f join f.organisation o where o.orgId=?1 group by d.depId")
    Set<Tuple> countLMSCoursesByDepOfOrgId(Long orgId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(lmsc) as count FROM LMSCourse lmsc left join lmsc.course c join c.department d join d.faculty f join f.organisation o group by d.depId")
    Set<Tuple> countLMSCoursesByDepOfRatos();

    // --------------------------------------------------ADMIN ---------------------------------------------------------
    @Query(value = "SELECT c FROM LMSCourse c join fetch c.course co join fetch co.staff s join fetch s.user join fetch s.position join fetch co.access a join fetch c.lms", countQuery = "SELECT count(c) FROM LMSCourse c")
    Page<LMSCourse> findAll(Pageable pageable);
}

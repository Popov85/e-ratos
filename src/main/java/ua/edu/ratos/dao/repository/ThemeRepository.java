package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ratos.dao.entity.Theme;

import jakarta.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    // For security purposes to check "modify"-access to a theme
    @Query(value = "SELECT t FROM Theme t join fetch t.access a join fetch t.staff s join fetch t.department d where t.themeId =?1")
    Optional<Theme> findForSecurityById(Long themeId);

    // -----------------------------------------------------Staff (drop down)-------------------------------------------
    @Query(value = "SELECT new Theme(t.themeId, t.name) FROM Theme t join  t.staff s where s.staffId =?1")
    Set<Theme> findAllForDropDownByStaffId(Long staffId);

    @Query(value = "SELECT new Theme(t.themeId, t.name) FROM Theme t join t.department d where d.depId =?1")
    Set<Theme> findAllForDropDownByDepartmentId(Long depId);

    // -----------------------------------------------------Staff (table)-----------------------------------------------
    @Query(value = "SELECT t FROM Theme t join fetch t.staff s join fetch s.user join fetch s.position join fetch t.department d join fetch t.course c left join fetch c.lmsCourse join fetch t.access a where d.depId =?1")
    Set<Theme> findAllForTableByDepartmentId(Long depId);

    @Query(value = "SELECT size(q) FROM Theme t join t.questions q where t.themeId =?1")
    Long calculateQuestionsByThemeId(Long themeId);

    //-------------------------------------------------REPORT on content------------------------------------------------
    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(t) as count FROM Theme t left join t.department d join d.faculty f join f.organisation o where d.depId=?1 group by d.depId")
    Tuple countThemesByDepOfDepId(Long depId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(t) as count FROM Theme t left join t.department d join d.faculty f join f.organisation o where f.facId=?1 group by d.depId")
    Set<Tuple> countThemesByDepOfFacId(Long facId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(t) as count FROM Theme t left join t.department d join d.faculty f join f.organisation o where o.orgId=?1 group by d.depId")
    Set<Tuple> countThemesByDepOfOrgId(Long orgId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(t) as count FROM Theme t left join t.department d join d.faculty f join f.organisation o group by d.depId")
    Set<Tuple> countThemesByDepOfRatos();

    // -----------------------------------------------------ADMIN-TEST -------------------------------------------------
    // (for simple table: no organisation, no faculty, no department data)
    @Query(value = "SELECT t FROM Theme t join fetch t.staff s join fetch s.user join fetch s.position join fetch t.course c join fetch t.access", countQuery = "SELECT count(t) FROM Theme t")
    Page<Theme> findAllAdmin(Pageable pageable);
}

package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import ua.edu.ratos.dao.entity.Scheme;
import jakarta.persistence.QueryHint;
import jakarta.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

public interface SchemeRepository extends JpaRepository<Scheme, Long> {

    //TODO: to revisit and remove!
    //@Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.strategy join fetch s.grading join fetch s.options join fetch s.access join fetch s.course c left join fetch c.lmsCourse join fetch s.staff st join fetch st.user join fetch st.position join fetch s.themes st join fetch st.theme join fetch st.settings sts join fetch sts.type left join fetch s.groups where s.schemeId = ?1")
    //Optional<Scheme> findForEditById(Long schemeId);

    //TODO: check as GTP generated !
    @Query("SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.strategy join fetch s.grading join fetch s.options join fetch s.access join fetch s.course c left join fetch c.lmsCourse join fetch s.staff staff join fetch staff.user join fetch staff.position join fetch s.themes themes join fetch themes.theme join fetch themes.settings themeSettings join fetch themeSettings.type left join fetch s.groups where s.schemeId = ?1")
    Optional<Scheme> findForEditById(Long schemeId);

    // For security purposes to check "modify"-access
    @Query(value = "SELECT s FROM Scheme s join fetch s.access a join fetch s.staff st join fetch st.user join fetch st.department d where s.schemeId =?1")
    Optional<Scheme> findForSecurityById(Long schemeId);

    // -----------------------------------------One for different purposes----------------------------------------------
    @Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.options join fetch s.strategy join fetch s.grading join fetch s.themes st join fetch st.settings left join fetch s.groups g left join fetch g.students where s.schemeId = ?1")
    @QueryHints({@QueryHint(name="jakarta.persistence.cache.storeMode", value="USE"), @QueryHint(name="jakarta.persistence.cache.retrieveMode", value="USE")})
    Optional<Scheme> findForSessionById(Long schemeId);

    @Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.options join fetch s.strategy join fetch s.grading join fetch s.themes st join fetch st.settings sts join fetch sts.type where s.schemeId = ?1")
    @QueryHints({@QueryHint(name="jakarta.persistence.cache.storeMode", value="USE"), @QueryHint(name="jakarta.persistence.cache.retrieveMode", value="USE")})
    Optional<Scheme> findForInfoById(Long schemeId);

    @Query(value = "SELECT s FROM Scheme s join fetch s.themes t join fetch t.settings where s.schemeId = ?1")
    Optional<Scheme> findForThemesManipulationById(Long schemeId);

    @Query(value = "SELECT s FROM Scheme s left join fetch s.grading t where s.schemeId = ?1")
    Optional<Scheme> findForGradingById(Long schemeId);

    //--------------------------------------------Instructors (for table)-----------------------------------------------
    @Query(value = "SELECT s FROM Scheme s join fetch s.strategy join fetch s.settings join fetch s.options join fetch s.mode join fetch s.grading join fetch s.course c left join fetch c.lmsCourse join fetch s.staff st join fetch st.user join fetch st.position join s.department d join fetch s.access left join fetch s.themes left join fetch s.groups where d.depId =?1")
    Set<Scheme> findAllByDepartmentId(Long depId);

    //-------------------------------------------Populate cache on start-up---------------------------------------------
    @Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.options join fetch s.strategy join fetch s.grading join fetch s.themes st join fetch st.settings left join fetch s.groups g left join fetch g.students")
    @QueryHints({@QueryHint(name="jakarta.persistence.cache.storeMode", value="REFRESH"), @QueryHint(name="jakarta.persistence.cache.retrieveMode", value="BYPASS")})
    Slice<Scheme> findAllForCachedSession(Pageable pageable);

    @Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.options join fetch s.strategy join fetch s.grading join fetch s.themes st join fetch st.settings left join fetch s.groups g left join fetch g.students where size(s.themes)>1")
    @QueryHints({@QueryHint(name="jakarta.persistence.cache.storeMode", value="REFRESH"), @QueryHint(name="jakarta.persistence.cache.retrieveMode", value="BYPASS")})
    Slice<Scheme> findLargeForCachedSession(Pageable pageable);

    //--------------------------------------------Populate cache at run-time--------------------------------------------
    @Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.options join fetch s.strategy join fetch s.grading join fetch s.themes st join fetch st.settings left join fetch s.groups g left join fetch g.students join s.course c where c.courseId=?1")
    @QueryHints({@QueryHint(name="jakarta.persistence.cache.storeMode", value="REFRESH"), @QueryHint(name="jakarta.persistence.cache.retrieveMode", value="BYPASS")})
    Slice<Scheme> findCoursesSchemesForCachedSession(Long courseId, Pageable pageable);

    @Query(value = "SELECT s FROM Scheme s join fetch s.mode join fetch s.settings join fetch s.options join fetch s.strategy join fetch s.grading join fetch s.themes st join fetch st.settings left join fetch s.groups g left join fetch g.students join s.department d where d.depId=?1")
    @QueryHints({@QueryHint(name="jakarta.persistence.cache.storeMode", value="REFRESH"), @QueryHint(name="jakarta.persistence.cache.retrieveMode", value="BYPASS")})
    Slice<Scheme> findDepartmentSchemesForCachedSession(Long depId, Pageable pageable);

    //-------------------------------------------------REPORT on content------------------------------------------------
    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(s) as count FROM Scheme s left join s.department d join d.faculty f join f.organisation o where d.depId=?1 group by d.depId")
    Tuple countSchemesByDepOfDepId(Long depId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(s) as count FROM Scheme s left join s.department d join d.faculty f join f.organisation o where f.facId=?1 group by d.depId")
    Set<Tuple> countSchemesByDepOfFacId(Long facId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(s) as count FROM Scheme s left join s.department d join d.faculty f join f.organisation o where o.orgId=?1 group by d.depId")
    Set<Tuple> countSchemesByDepOfOrgId(Long orgId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(s) as count FROM Scheme s left join s.department d join d.faculty f join f.organisation o group by d.depId")
    Set<Tuple> countSchemesByDepOfRatos();

    //----------------------------------------------Set (min for drop-down)---------------------------------------------
    // TODO: select only id and name!!!

    @Query(value = "SELECT new Scheme(s.schemeId, s.name) FROM Scheme s join s.staff st where st.staffId =?1")
    Set<Scheme> findAllForDropDownByStaffId(Long staffId);

    @Query(value = "SELECT new Scheme(s.schemeId, s.name) FROM Scheme s join s.department d where d.depId =?1")
    Set<Scheme> findAllForDropDownByDepartmentId(Long depId);

    @Query(value = "SELECT new Scheme(s.schemeId, s.name) FROM Scheme s join s.course c where c.courseId =?1")
    Set<Scheme> findAllForDropDownByCourseId(Long courseId);

    //------------------------------------------ADMIN-TABLE to replace by Specs-----------------------------------------
    // (for simple table: no organisation, no faculty, no department data)
    @Query(value = "SELECT s FROM Scheme s join fetch s.strategy join fetch s.settings join fetch s.options join fetch s.mode join fetch s.grading join fetch s.course join fetch s.staff st join fetch st.user join fetch st.position join fetch s.access left join fetch s.themes left join fetch s.groups", countQuery = "SELECT count(s) FROM Scheme s")
    Page<Scheme> findAll(Pageable pageable);

}

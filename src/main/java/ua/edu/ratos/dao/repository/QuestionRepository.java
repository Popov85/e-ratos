package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import ua.edu.ratos.dao.entity.question.*;
import ua.edu.ratos.dao.repository.projections.TypeAndCount;

import jakarta.persistence.QueryHint;
import jakarta.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public interface QuestionRepository extends JpaRepository<Question, Long> {

    //------------------------------------------Count for deciding question provider impl-------------------------------
    @Query(value = "select count(q) from Question q join q.theme t where t.themeId = ?1")
    int countByThemeId(Long themeId);

    //-------------------------------------------------Scheme creating support------------------------------------------
    @Query(value = "select ty.typeId as type, ty.abbreviation as abbreviation, count(q) as count from Question q join q.theme th join q.type ty where th.themeId = ?1 group by ty")
    Set<TypeAndCount> countAllTypesByThemeId(Long themeId);

    @Query(value = "SELECT q FROM Question q join fetch q.type join q.theme t where t.themeId = ?1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Set<Question> findAllForTypeLevelMapByThemeId(Long themeId);

    //------------------------------------------------Student session simple (L2C)--------------------------------------
    @Query(value = "SELECT q FROM Question q join q.theme t join q.type ty where t.themeId = ?1 and ty.typeId = ?2 and q.level =?3")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Set<Question> findAllForSimpleSessionByThemeTypeAndLevel(Long themeId, Long typeId, byte level);

    //--------------------------------------------Student session cached (Spring Cache)---------------------------------
    @Query(value = "SELECT q FROM QuestionMCQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a left join fetch a.resources where t.themeId = ?1 and ty.typeId=1 and q.level =?2")
    @QueryHints({@QueryHint(name="javax.persistence.cache.storeMode", value="BYPASS"), @QueryHint(name="javax.persistence.cache.retrieveMode", value="BYPASS")})
    Set<QuestionMCQ> findAllMCQForCachedSessionWithEverythingByThemeAndLevel(Long themeId, byte level);

    @Query(value = "SELECT q FROM QuestionFBSQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answer a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where t.themeId = ?1 and ty.typeId=2 and q.level =?2")
    @QueryHints({@QueryHint(name="javax.persistence.cache.storeMode", value="BYPASS"), @QueryHint(name="javax.persistence.cache.retrieveMode", value="BYPASS")})
    Set<QuestionFBSQ> findAllFBSQForCachedSessionWithEverythingByThemeAndLevel(Long themeId, byte level);

    @Query(value = "SELECT q FROM QuestionFBMQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where t.themeId = ?1 and ty.typeId=3 and q.level =?2")
    @QueryHints({@QueryHint(name="javax.persistence.cache.storeMode", value="BYPASS"), @QueryHint(name="javax.persistence.cache.retrieveMode", value="BYPASS")})
    Set<QuestionFBMQ> findAllFBMQForCachedSessionWithEverythingByThemeAndLevel(Long themeId, byte level);

    @Query(value = "SELECT q FROM QuestionMQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.leftPhrase lp left join fetch lp.resources join fetch a.rightPhrase rp left join fetch rp.resources where t.themeId = ?1 and ty.typeId=4 and q.level =?2")
    @QueryHints({@QueryHint(name="javax.persistence.cache.storeMode", value="BYPASS"), @QueryHint(name="javax.persistence.cache.retrieveMode", value="BYPASS")})
    Set<QuestionMQ> findAllMQForCachedSessionWithEverythingByThemeAndLevel(Long themeId, byte level);

    @Query(value = "SELECT q FROM QuestionSQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.phrase p left join fetch p.resources r where t.themeId = ?1 and ty.typeId=5 and q.level =?2")
    @QueryHints({@QueryHint(name="javax.persistence.cache.storeMode", value="BYPASS"), @QueryHint(name="javax.persistence.cache.retrieveMode", value="BYPASS")})
    Set<QuestionSQ> findAllSQForCachedSessionWithEverythingByThemeAndLevel(Long themeId, byte level);

    //---------------------------------------------------------One for edit---------------------------------------------
    @Query(value = "SELECT q FROM QuestionMCQ q join fetch q.theme join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a left join fetch a.resources where q.questionId =?1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<QuestionMCQ> findOneMCQForEditById(Long questionId);

    @Query(value = "SELECT q FROM QuestionFBSQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answer a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where q.questionId =?1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<QuestionFBSQ> findOneFBSQForEditById(Long questionId);

    @Query(value = "SELECT q FROM QuestionFBMQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where q.questionId =?1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<QuestionFBMQ> findOneFBMQForEditById(Long questionId);

    @Query(value = "SELECT q FROM QuestionMQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.leftPhrase lp left join fetch lp.resources join fetch a.rightPhrase rp left join fetch rp.resources where q.questionId =?1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<QuestionMQ> findOneMQForEditById(Long questionId);

    @Query(value = "SELECT q FROM QuestionSQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.phrase p left join fetch p.resources r where q.questionId =?1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<QuestionSQ> findOneSQForEditById(Long questionId);


    //-------------------------------------------------------Instructor table-------------------------------------------

    @Query(value = "SELECT q FROM QuestionMCQ q join fetch q.theme join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a left join fetch a.resources where q.theme.themeId =?1 and ty.typeId=1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Set<QuestionMCQ> findAllMCQForEditByThemeId(Long themeId);

    // Left for scaling only! Not used now
    @Query(value = "SELECT q FROM QuestionMCQ q join fetch q.theme join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a left join fetch a.resources where q.theme.themeId =?1 and ty.typeId=1", countQuery = "SELECT count(q) FROM Question q join q.theme t join q.type ty where t.themeId=?1 and ty.typeId=1")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<QuestionMCQ> findAllMCQForEditByThemeId(Long themeId, Pageable pageable);

    // Left for scaling only! Not used now
    @Query(value = "SELECT q FROM QuestionFBSQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answer a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where t.themeId = ?1 and ty.typeId=2", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t where t.themeId=?1 and ty.typeId=2")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<QuestionFBSQ> findAllFBSQForEditByThemeId(Long themeId, Pageable pageable);

    // Left for scaling only! Not used now
    @Query(value = "SELECT q FROM QuestionFBMQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where t.themeId = ?1 and ty.typeId=3", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t where t.themeId=?1 and ty.typeId=3")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<QuestionFBMQ> findAllFBMQForEditByThemeId(Long themeId, Pageable pageable);

    // Left for scaling only! Not used now
    @Query(value = "SELECT q FROM QuestionMQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.leftPhrase lp left join fetch lp.resources join fetch a.rightPhrase rp left join fetch rp.resources where t.themeId = ?1 and ty.typeId=4", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t where t.themeId=?1 and ty.typeId=4")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<QuestionMQ> findAllMQForEditByThemeId(Long themeId, Pageable pageable);

    // Left for scaling only! Not used now
    @Query(value = "SELECT q FROM QuestionSQ q join fetch q.theme t join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.phrase p left join fetch p.resources r where t.themeId = ?1 and ty.typeId=5", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t where t.themeId=?1 and ty.typeId=5")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<QuestionSQ> findAllSQForEditByThemeId(Long themeId, Pageable pageable);


    //----------------------------------Instructor (global search throughout department)--------------------------------
    // first slice of 10
    @Query(value = "SELECT q FROM QuestionMCQ q join fetch q.theme t join t.department d join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a left join fetch a.resources where d.depId =?1 and ty.typeId=1 and q.question like %?2%", countQuery = "SELECT count(q) FROM Question q join q.theme t join t.department d join q.type ty where d.depId=?1 and ty.typeId=1 and q.question like %?2%")
    Slice<QuestionMCQ> findAllMCQForSearchByDepartmentIdAndTitleContains(Long depId, String starts, Pageable pageable);

    @Query(value = "SELECT q FROM QuestionFBSQ q join fetch q.theme t join t.department d join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answer a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where d.depId = ?1 and ty.typeId=2 and q.question like %?2%", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t join t.department d where d.depId=?1 and ty.typeId=2 and q.question like %?2%")
    Slice<QuestionFBSQ> findAllFBSQForSearchByDepartmentIdAndTitleContains(Long depId, String starts, Pageable pageable);

    @Query(value = "SELECT q FROM QuestionFBMQ q join fetch q.theme t join t.department d join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.settings join fetch a.acceptedPhrases p left join fetch p.resources r where d.depId = ?1 and ty.typeId=3 and q.question like %?2%", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t join t.department d where d.depId=?1 and ty.typeId=3 and q.question like %?2%")
    Slice<QuestionFBMQ> findAllFBMQForSearchByDepartmentIdAndTitleContains(Long depId, String starts, Pageable pageable);

    @Query(value = "SELECT q FROM QuestionMQ q join fetch q.theme t join t.department d join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.leftPhrase lp left join fetch lp.resources join fetch a.rightPhrase rp left join fetch rp.resources where d.depId = ?1 and ty.typeId=4 and q.question like %?2%", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t join t.department d where d.depId=?1 and ty.typeId=4 and q.question like %?2%")
    Slice<QuestionMQ> findAllMQForSearchByDepartmentIdAndTitleContains(Long depId, String starts, Pageable pageable);

    @Query(value = "SELECT q FROM QuestionSQ q join fetch q.theme t join t.department d join fetch q.type ty left join fetch q.helps h left join fetch h.resources left join fetch q.resources" +
            " join fetch q.answers a join fetch a.phrase p left join fetch p.resources r where d.depId = ?1 and ty.typeId=5 and q.question like %?2%", countQuery = "SELECT count(q) FROM Question q join q.type ty join q.theme t join t.department d where d.depId=?1 and ty.typeId=5 and q.question like %?2%")
    Slice<QuestionSQ> findAllSQForSearchByDepartmentIdAndTitleContains(Long depId, String starts, Pageable pageable);


    //-------------------------------------------------REPORT on content------------------------------------------------
    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(q) as count FROM Question q join q.theme t join t.department d join d.faculty f join f.organisation o where d.depId=?1 group by d.depId")
    Tuple countQuestionsByDepOfDepId(Long depId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(q) as count FROM Question q join q.theme t join t.department d join d.faculty f join f.organisation o where f.facId=?1 group by d.depId")
    Set<Tuple> countQuestionsByDepOfFacId(Long facId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(q) as count FROM Question q join q.theme t join t.department d join d.faculty f join f.organisation o where o.orgId=?1 group by d.depId")
    Set<Tuple> countQuestionsByDepOfOrgId(Long orgId);

    @Query(value = "SELECT o.name as org, f.name as fac, d.name as dep, count(q) as count FROM Question q join q.theme t join t.department d join d.faculty f join f.organisation o group by d.depId")
    Set<Tuple> countQuestionsByDepOfRatos();
}
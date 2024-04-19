package ua.edu.ratos.dao.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.Tuple;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Scheme;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SchemeRepositoryTestIT extends BaseIT {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private PersistenceUnitUtil persistenceUnitUtil;

    @BeforeEach
    public void setUp() {
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one_groups_themes.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForEditByIdTest() {
        Optional<Scheme> optional = schemeRepository.findForEditById(1L);
        assertTrue(optional.isPresent(), "Scheme is not found");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "mode"), "Mode of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "strategy"), "Strategy of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "grading"), "Grading of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "options"), "Options of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "access"), "Access of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "themes"), "Themes of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "groups"), "Groups of Scheme is not loaded");
    }

    //------------------------------------------------------SECURITY----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForSecurityByIdTest() {
        Optional<Scheme> optional = schemeRepository.findForSecurityById(1L);
        assertTrue(optional.isPresent(), "Scheme is not found");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "access"), "Access of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "staff"), "Staff of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get().getStaff(), "user"), "User of Staff of Scheme is not loaded");
    }

    //-------------------------------------------------------SESSION----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one_session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForSessionByIdTest() {
        Optional<Scheme> optional = schemeRepository.findForSessionById(1L);
        assertTrue(optional.isPresent(), "Scheme is not found");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "mode"), "Mode of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "strategy"), "Strategy of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "grading"), "Grading of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "options"), "Options of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "themes"), "Themes of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "groups"), "Groups of Scheme is not loaded");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one_session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForInfoByIdTest() {
        Optional<Scheme> optional = schemeRepository.findForInfoById(1L);
        assertTrue(optional.isPresent(), "Scheme is not found");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "mode"), "Mode of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "strategy"), "Strategy of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "grading"), "Grading of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "options"), "Options of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "themes"), "Themes of Scheme is not loaded");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one_session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForThemesManipulationByIdTest() {
        Optional<Scheme> optional = schemeRepository.findForThemesManipulationById(1L);
        assertTrue(optional.isPresent(), "Scheme is not found");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "themes"), "Themes of Scheme is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get().getThemes(), "settings"), "Settings of Themes of Scheme is not loaded");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_one_session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForGradingByIdTest() {
        Optional<Scheme> optional = schemeRepository.findForGradingById(1L);
        assertTrue(optional.isPresent(), "Scheme is not found");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "grading"), "Grading of Scheme is not loaded");
    }

    //-------------------------------------------------------CACHE------------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_with_themes_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findLargeForCachedSessionTest() {
        assertThat("Slice of Scheme is not of size = 2",
                schemeRepository.findLargeForCachedSession(PageRequest.of(0, 10)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_with_themes_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findCoursesSchemesForCachedSessionTest() {
        assertThat("Slice of Scheme is not of size = 2",
                schemeRepository.findCoursesSchemesForCachedSession(2L, PageRequest.of(0, 10)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_with_themes_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findDepartmentSchemesForCachedSessionTest() {
        assertThat("Slice of Scheme is not of size = 2",
                schemeRepository.findDepartmentSchemesForCachedSession(2L, PageRequest.of(0, 10)).getContent(), hasSize(2));
    }

    //--------------------------------------------------INSTRUCTOR table----------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Set of Scheme is not of size = 10 for depId = 1",
                schemeRepository.findAllByDepartmentId(1L), hasSize(10));
    }

    //---------------------------------------------REPORT on content----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countSchemesByDepOfDepId() {
        Tuple schemesByDep = schemeRepository.countSchemesByDepOfDepId(1L);
        MatcherAssert.assertThat("Org. name is not as expected", schemesByDep.get("org"), equalTo("University"));
        MatcherAssert.assertThat("Fac. name is not as expected", schemesByDep.get("fac"), equalTo("Faculty"));
        MatcherAssert.assertThat("Dep. name is not as expected", schemesByDep.get("dep"), equalTo("Department"));
        MatcherAssert.assertThat("Count of schemes is not as expected", schemesByDep.get("count"), equalTo(10L));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countSchemesByDepOfFacId() {
        Set<Tuple> schemesByDep = schemeRepository.countSchemesByDepOfFacId(1L);
        MatcherAssert.assertThat("Count tuple of schemes by dep is not of right size", schemesByDep, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countSchemesByDepOfFacIdNegative() {
        Set<Tuple> schemesByDep = schemeRepository.countSchemesByDepOfFacId(2L);
        MatcherAssert.assertThat("Count tuple of schemes by dep is not empty", schemesByDep, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countSchemesByDepOfOrgId() {
        Set<Tuple> schemesByDep = schemeRepository.countSchemesByDepOfOrgId(1L);
        MatcherAssert.assertThat("Count tuple of schemes by dep is not of right size", schemesByDep, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countSchemesByDepOfOrgIdNegative() {
        Set<Tuple> schemesByDep = schemeRepository.countSchemesByDepOfOrgId(2L);
        MatcherAssert.assertThat("Count tuple of schemes by dep is not empty", schemesByDep, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/scheme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countSchemesByDepOfRatos() {
        Set<Tuple> schemesByDep = schemeRepository.countSchemesByDepOfRatos();
        MatcherAssert.assertThat("Count tuple of schemes by dep is not of right size", schemesByDep, hasSize(3));
    }
}

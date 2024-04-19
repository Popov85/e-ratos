package ua.edu.ratos.dao.repository;

import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ThemeRepositoryTestIT extends BaseIT {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForSecurityByIdTest() {
        assertTrue(themeRepository.findForSecurityById(1L).isPresent(), "Theme is not found");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllForDropDownByStaffIdTest() {
        assertThat("Set of Theme is not of size = 11",
                themeRepository.findAllForDropDownByStaffId(1L), hasSize(11));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllForDropDownByDepartmentIdTest() {
        assertThat("Page of Theme is not of size = 5",
                themeRepository.findAllForDropDownByDepartmentId(3L), hasSize(5));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllForTableByDepartmentIdTest() {
        assertThat("Page of Theme is not of size = 5",
                themeRepository.findAllForTableByDepartmentId(3L), hasSize(5));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void calculateQuestionsByThemeIdTest() {
        assertThat("The quantity of questions for theme is not as expected",
                themeRepository.calculateQuestionsByThemeId(1L), equalTo(3L));
    }


    //---------------------------------------------REPORT on content----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countThemesByDepOfDepId() {
        Tuple themesByDep = themeRepository.countThemesByDepOfDepId(1L);
        assertThat("Org. name is not as expected", themesByDep.get("org"), equalTo("University"));
        assertThat("Fac. name is not as expected", themesByDep.get("fac"), equalTo("Faculty"));
        assertThat("Dep. name is not as expected", themesByDep.get("dep"), equalTo("Department"));
        assertThat("Count of themes is not as expected", themesByDep.get("count"), equalTo(11L));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countThemesByDepOfFacId() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfFacId(1L);
        assertThat("Count tuple of themes by dep is not of right size", themesByDeps, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countThemesByDepOfFacIdNegative() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfFacId(2L);
        assertThat("Count tuple of themes by dep is not empty", themesByDeps, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countThemesByDepOfOrgId() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfOrgId(1L);
        assertThat("Count tuple of themes by dep is not of right size", themesByDeps, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countThemesByDepOfOrgIdNegative() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfOrgId(2L);
        assertThat("Count tuple of themes by dep is not empty", themesByDeps, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countThemesByDepOfRatos() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfRatos();
        assertThat("Count tuple of themes by dep is not of right size", themesByDeps, hasSize(3));
    }

    //----------------------------------------------------Admin---------------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllAdminTest() {
        assertThat("Page of Theme is not of size = 21",
                themeRepository.findAllAdmin(PageRequest.of(0, 100)).getContent(), hasSize(21));
    }

}

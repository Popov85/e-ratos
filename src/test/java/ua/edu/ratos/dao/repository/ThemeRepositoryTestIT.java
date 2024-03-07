package ua.edu.ratos.dao.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ua.edu.ratos.ActiveProfile;

import javax.persistence.Tuple;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ThemeRepositoryTestIT {

    @Autowired
    private ThemeRepository themeRepository;

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findForSecurityByIdTest() {
        assertTrue("Theme is not found", themeRepository.findForSecurityById(1L).isPresent());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForDropDownByStaffIdTest() {
        assertThat("Set of Theme is not of size = 11",
                themeRepository.findAllForDropDownByStaffId(1L), hasSize(11));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForDropDownByDepartmentIdTest() {
        assertThat("Page of Theme is not of size = 5",
                themeRepository.findAllForDropDownByDepartmentId(3L), hasSize(5));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllForTableByDepartmentIdTest() {
        assertThat("Page of Theme is not of size = 5",
                themeRepository.findAllForTableByDepartmentId(3L), hasSize(5));
    }

    //@Ignore("Not ready")
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_mcq_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void calculateQuestionsByThemeIdTest() {
        assertThat("The quantity of questions for theme is not as expected",
                themeRepository.calculateQuestionsByThemeId(1L), equalTo(3L));
    }


    //---------------------------------------------REPORT on content----------------------------------------------------
    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countThemesByDepOfDepId() {
        Tuple themesByDep = themeRepository.countThemesByDepOfDepId(1L);
        assertThat("Org. name is not as expected", themesByDep.get("org"), equalTo("University"));
        assertThat("Fac. name is not as expected", themesByDep.get("fac"), equalTo("Faculty"));
        assertThat("Dep. name is not as expected", themesByDep.get("dep"), equalTo("Department"));
        assertThat("Count of themes is not as expected", themesByDep.get("count"), equalTo(11L));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countThemesByDepOfFacId() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfFacId(1L);
        assertThat("Count tuple of themes by dep is not of right size", themesByDeps, hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countThemesByDepOfFacIdNegative() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfFacId(2L);
        assertThat("Count tuple of themes by dep is not empty", themesByDeps, empty());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countThemesByDepOfOrgId() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfOrgId(1L);
        assertThat("Count tuple of themes by dep is not of right size", themesByDeps, hasSize(3));
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countThemesByDepOfOrgIdNegative() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfOrgId(2L);
        assertThat("Count tuple of themes by dep is not empty", themesByDeps, empty());
    }

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countThemesByDepOfRatos() {
        Set<Tuple> themesByDeps = themeRepository.countThemesByDepOfRatos();
        assertThat("Count tuple of themes by dep is not of right size", themesByDeps, hasSize(3));
    }

    //----------------------------------------------------Admin---------------------------------------------------------

    @Test(timeout = 5000)
    @Sql(scripts = {"/scripts/init.sql", "/scripts/theme_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllAdminTest() {
        assertThat("Page of Theme is not of size = 21",
                themeRepository.findAllAdmin(PageRequest.of(0, 100)).getContent(), hasSize(21));
    }

}

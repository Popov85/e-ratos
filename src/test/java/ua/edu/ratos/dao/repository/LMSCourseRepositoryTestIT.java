package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.lms.LMSCourse;
import ua.edu.ratos.dao.repository.lms.LMSCourseRepository;

import javax.persistence.Tuple;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class LMSCourseRepositoryTestIT {

    @Autowired
    private LMSCourseRepository lmsCourseRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findForSecurityByIdTest() {
        Optional<LMSCourse> optional = lmsCourseRepository.findForSecurityById(1L);
        assertTrue(optional.isPresent(), "Course was not found with courseId = 1L");
    }

    //---------------------------------------------REPORT on content----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countLMSCoursesByDepOfDepId() {
        Tuple coursesByDep = lmsCourseRepository.countLMSCoursesByDepOfDepId(1L);
        assertThat("Org. name is not as expected", coursesByDep.get("org"), equalTo("University"));
        assertThat("Fac. name is not as expected", coursesByDep.get("fac"), equalTo("Faculty"));
        assertThat("Dep. name is not as expected", coursesByDep.get("dep"), equalTo("Department"));
        assertThat("Count of courses is not as expected", coursesByDep.get("count"), equalTo(5L));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countLMSCoursesByDepOfFacId() {
        Set<Tuple> coursesByDeps = lmsCourseRepository.countLMSCoursesByDepOfFacId(1L);
        assertThat("Count tuple of courses by dep is not of right size", coursesByDeps, hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countLMSCoursesByDepOfFacIdNegative() {
        Set<Tuple> coursesByDeps = lmsCourseRepository.countLMSCoursesByDepOfFacId(2L);
        assertThat("Count tuple of courses by dep is not empty", coursesByDeps, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countLMSCoursesByDepOfOrgId() {
        Set<Tuple> coursesByDeps = lmsCourseRepository.countLMSCoursesByDepOfOrgId(1L);
        assertThat("Count tuple of courses by dep is not of right size", coursesByDeps, hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countLMSCoursesByDepOfOrgIdNegative() {
        Set<Tuple> coursesByDeps = lmsCourseRepository.countLMSCoursesByDepOfOrgId(2L);
        assertThat("Count tuple of courses by dep is not empty", coursesByDeps, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countLMSCoursesByDepOfRatos() {
        Set<Tuple> coursesByDeps = lmsCourseRepository.countLMSCoursesByDepOfRatos();
        assertThat("Count tuple of courses by dep is not of right size", coursesByDeps, hasSize(2));
    }

    //----------------------------------------------------ADMIN---------------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_lms_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllAdminTest() {
        assertThat("Page of LMSCourse is not of size = 10",
                lmsCourseRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(10));
    }
}

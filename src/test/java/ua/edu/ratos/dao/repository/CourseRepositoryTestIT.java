package ua.edu.ratos.dao.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Course;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRepositoryTestIT extends BaseIT {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private PersistenceUnitUtil persistenceUnitUtil;

    @BeforeEach
    public void setUp() {
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    //-------------------------------------------------SECURITY-------------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findForSecurityByIdTest() {
        Optional<Course> course = courseRepository.findForSecurityById(1L);
        // Fetched Access, Staff, Department
        assertTrue(persistenceUnitUtil.isLoaded(course.orElseThrow(), "access"), "Access of Course is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(course.orElseThrow(), "staff"), "Staff of Course is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(course.orElseThrow().getStaff(), "user"), "User of Staff is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(course.orElseThrow().getStaff(), "department"), "Department of Staff is not loaded");
        assertThat("Course object is not as expected", course.get(), allOf(
                hasProperty("courseId", equalTo(1L)),
                hasProperty("name", equalTo("Test LTI course #1")),
                hasProperty("created", is(notNullValue())),
                hasProperty("deleted", equalTo(false)))
        );
    }

    //--------------------------------------------------DROPDOWN min----------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllMinForDropDownByStaffIdTest() {
        Set<Course> all = courseRepository.findAllForDropDownByStaffId(1L);
        assertThat("Set of Courses is not of right size", all, hasSize(8));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllMinForDropDownByDepartmentIdTest() {
        Set<Course> all = courseRepository.findAllForDropDownByDepartmentId(3L);
        assertThat("Set of Courses is not of right size", all, hasSize(8));
    }

    //---------------------------------------------------Staff table----------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllForTableByDepartmentId() {
        Set<Course> result = courseRepository.findAllForTableByDepartmentId(3L);
        assertThat("Set of Courses is not as expected", result.size(), equalTo(8));
    }

    //---------------------------------------------REPORT on content----------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countCoursesByDepOfDepId() {
        Tuple coursesByDep = courseRepository.countCoursesByDepOfDepId(1L);
        assertThat("Org. name is not as expected", coursesByDep.get("org"), equalTo("University"));
        assertThat("Fac. name is not as expected", coursesByDep.get("fac"), equalTo("Faculty"));
        assertThat("Dep. name is not as expected", coursesByDep.get("dep"), equalTo("Department"));
        assertThat("Count of courses is not as expected", coursesByDep.get("count"), equalTo(8L));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countCoursesByDepOfFacId() {
        Set<Tuple> coursesByDeps = courseRepository.countCoursesByDepOfFacId(1L);
        assertThat("Count tuple of courses by dep is not of right size", coursesByDeps, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countCoursesByDepOfFacIdNegative() {
        Set<Tuple> coursesByDeps = courseRepository.countCoursesByDepOfFacId(2L);
        assertThat("Count tuple of courses by dep is not empty", coursesByDeps, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countCoursesByDepOfOrgId() {
        Set<Tuple> coursesByDeps = courseRepository.countCoursesByDepOfOrgId(1L);
        assertThat("Count tuple of courses by dep is not of right size", coursesByDeps, hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countCoursesByDepOfOrgIdNegative() {
        Set<Tuple> coursesByDeps = courseRepository.countCoursesByDepOfOrgId(2L);
        assertThat("Count tuple of courses by dep is not empty", coursesByDeps, empty());
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void countCoursesByDepOfRatos() {
        Set<Tuple> coursesByDeps = courseRepository.countCoursesByDepOfRatos();
        assertThat("Count tuple of courses by dep is not of right size", coursesByDeps, hasSize(3));
    }

    //--------------------------------------------------------ADMIN-----------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/course_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllTest() {
        Page<Course> page = courseRepository.findAll(PageRequest.of(0, 100));
        assertThat("Page of Courses is not as expected", page, allOf(
                hasProperty("content", hasSize(21)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(21L))));
    }
}

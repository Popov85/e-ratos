package ua.edu.ratos.dao.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.ResultOfStudent;
import ua.edu.ratos.dao.repository.specs.ResultOfStudentStaffSpecs;
import ua.edu.ratos.dao.repository.specs.SpecsFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static ua.edu.ratos.dao.repository.specs.ResultPredicatesUtils.hasSpecs;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ResultOfStudentRepositorySpecificationsTestIT extends BaseIT {

    @Autowired
    private ResultOfStudentRepository resultOfStudentRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private PersistenceUnitUtil persistenceUnitUtil;

    @BeforeEach
    public void setUp() {
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentId1Test() {
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(1L);
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(6)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(6L))));
        page.getContent()
                .forEach(r -> {
                    assertTrue(persistenceUnitUtil.isLoaded(r, "scheme"), "Scheme was not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(r, "student"), "Student was not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(r, "resultDetails"), "ResultDetails was not loaded");
                    assertNotNull(r.getResultDetails(), "ResultDetails was empty");
                    assertTrue(persistenceUnitUtil.isLoaded(r.getStudent(), "user"), "User of Staff was not loaded");
                });
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentId2Test() {
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L);
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(7)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(7L))));
        page.getContent()
                .forEach(r -> {
                    assertTrue(persistenceUnitUtil.isLoaded(r, "scheme"), "Scheme was not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(r, "student"), "Student was not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(r, "resultDetails"), "ResultDetails was not loaded");
                    assertNull(r.getResultDetails(), "ResultDetails was not empty");
                    assertTrue(persistenceUnitUtil.isLoaded(r.getStudent(), "user"), "User of Staff was not loaded");
                });
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByCourseIdTest() {
        // Given: depId = 1L, courseId = 1L
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("1");
        specsMap.put("scheme.course", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(1L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(6)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(6L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsBySchemeIdTest() {
        // Given: depId = 1L, schemeId = 1L
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("1");
        specsMap.put("scheme", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(1L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(2)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(2L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByNameTest() {
        // Given: depId = 2L, name = "Den"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("Den");
        specsMap.put("student.user.name", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsBySurnameTest() {
        // Given: depId = 2L, surname = "Abram"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("Abram");
        specsMap.put("student.user.surname", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByEmailTest() {
        // Given: depId = 2L, email = "den.abramov@"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("den.abramov@");
        specsMap.put("student.user.email", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        page.getContent().forEach(System.out::println);
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByStudentFacIdTest() {
        // Given: depId = 2L, facId = 1
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("1");
        specsMap.put("student.faculty", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(7)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(7L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByStudentClassTest() {
        // Given: depId = 2L, studentClass = "Class"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("Class");
        specsMap.put("student.studentClass", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(7)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(7L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByStudentYearOfEntranceTest() {
        // Given: depId = 2L, YearOfEntrance = "2020"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("2020");
        specsMap.put("student.entranceYear", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsBySessionEndedTest() {
        // Given: depId = 2L, sessionEnded > = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        String from = "2019-09-01T00:00:00.000Z";
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        Map<String, String> date = new HashMap<>();
        date.put("date", from);
        date.put("comparator", ">");
        specsFilter.setFilterVal(date);
        specsMap.put("sessionEnded", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "sessionEnded")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsBySessionLastedTest() {
        // Given: depId = 2L, sessionLasted =51
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        Map<String, String> lasted = new HashMap<>();
        lasted.put("number", "51");
        lasted.put("comparator", "=");
        specsFilter.setFilterVal(lasted);
        specsMap.put("sessionLasted", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByGradeTest() {
        // Given: depId = 2L, grade = "5"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        Map<String, String> lasted = new HashMap<>();
        lasted.put("number", "5");
        lasted.put("comparator", "=");
        specsFilter.setFilterVal(lasted);
        specsMap.put("grade", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(4)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(4L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByPercentTest() {
        // Given: depId = 2L, percent > "80"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        Map<String, String> lasted = new HashMap<>();
        lasted.put("number", "80");
        lasted.put("comparator", ">");
        specsFilter.setFilterVal(lasted);
        specsMap.put("percent", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(4)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(4L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByPassedTest() {
        // Given: depId = 2L, passed = "true"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("true");
        specsMap.put("passed", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(7)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(7L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByNotPassedTest() {
        // Given: depId = 2L, passed = "false"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("false");
        specsMap.put("passed", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(0)),
                hasProperty("totalPages", equalTo(0)),
                hasProperty("totalElements", equalTo(0L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByTimeoutedTest() {
        // Given: depId = 2L, timeouted = "false"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("false");
        specsMap.put("timeouted", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(7)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(7L))));
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByNotCancelledTest() {
        // Given: depId = 2L, cancelled = "false"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("false");
        specsMap.put("cancelled", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(6)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(6L))));
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByLMSTest() {
        // Given: depId = 2L, lms = "true"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("true");
        specsMap.put("lms", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByNotLMSTest() {
        // Given: depId = 2L, lms = "false"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("false");
        specsMap.put("lms", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(6)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(6L))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdAndSpecsByPointsTest() {
        // Given: depId = 2L, points = "true"
        Map<String, SpecsFilter> specsMap = new HashMap<>();
        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("true");
        specsMap.put("points", specsFilter);
        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartment(2L)
                .and(hasSpecs(specsMap));
        Page<ResultOfStudent> page = resultOfStudentRepository.findAll(specs,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "grade")));
        assertThat("Page of Students Results is not as expected", page, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(1L))));
    }

    //-----------------------------------------------------Report on results--------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllOfRatosForReportBetweenDatesTest() {
        // Given: all ratos results, between dates "2018-12-20" and "2018-12-31"
        Map<String, SpecsFilter> specsMap = new HashMap<>();

        String from = "2018-12-20";
        SpecsFilter specsFilterFrom = new SpecsFilter();
        Map<String, String> dateFrom = new HashMap<>();
        dateFrom.put("date", from);
        specsFilterFrom.setFilterVal(dateFrom);

        String to = "2018-12-31";
        SpecsFilter specsFilterTo = new SpecsFilter();
        Map<String, String> dateTo = new HashMap<>();
        dateTo.put("date", to);
        specsFilterTo.setFilterVal(dateTo);

        specsMap.put("sessionEndedFrom", specsFilterFrom);
        specsMap.put("sessionEndedTo", specsFilterTo);

        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofRatosForReport().and(hasSpecs(specsMap));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        assertThat("Set of Students Results is not of right size", results, hasSize(5));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllOfOrganisationForReportBetweenDatesTest() {
        // Given: all orgId=1 results, between dates "2019-01-20" and "2019-01-30"
        Map<String, SpecsFilter> specsMap = new HashMap<>();

        String from = "2019-01-20";
        SpecsFilter specsFilterFrom = new SpecsFilter();
        Map<String, String> dateFrom = new HashMap<>();
        dateFrom.put("date", from);
        specsFilterFrom.setFilterVal(dateFrom);

        String to = "2019-01-30";
        SpecsFilter specsFilterTo = new SpecsFilter();
        Map<String, String> dateTo = new HashMap<>();
        dateTo.put("date", to);
        specsFilterTo.setFilterVal(dateTo);

        specsMap.put("sessionEndedFrom", specsFilterFrom);
        specsMap.put("sessionEndedTo", specsFilterTo);

        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofOrganisationForReport(1L).and(hasSpecs(specsMap));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        assertThat("Set of Students Results is not of right size", results, hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllOfFacultyForReportBetweenDatesTest() {
        // Given: all facId=1 results, between dates "2019-01-01" and "2019-01-30"
        Map<String, SpecsFilter> specsMap = new HashMap<>();

        String from = "2019-01-01";
        SpecsFilter specsFilterFrom = new SpecsFilter();
        Map<String, String> dateFrom = new HashMap<>();
        dateFrom.put("date", from);
        specsFilterFrom.setFilterVal(dateFrom);

        String to = "2019-01-30";
        SpecsFilter specsFilterTo = new SpecsFilter();
        Map<String, String> dateTo = new HashMap<>();
        dateTo.put("date", to);
        specsFilterTo.setFilterVal(dateTo);

        specsMap.put("sessionEndedFrom", specsFilterFrom);
        specsMap.put("sessionEndedTo", specsFilterTo);

        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofFacultyForReport(1L).and(hasSpecs(specsMap));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        assertThat("Set of Students Results is not of right size", results, hasSize(5));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllOfDepartmentForReportBetweenDatesTest() {
        // Given: all depId=2 results, between dates "2019-01-01" and "2020-01-01"
        Map<String, SpecsFilter> specsMap = new HashMap<>();

        String from = "2019-01-01";
        SpecsFilter specsFilterFrom = new SpecsFilter();
        Map<String, String> dateFrom = new HashMap<>();
        dateFrom.put("date", from);
        specsFilterFrom.setFilterVal(dateFrom);

        String to = "2020-01-01";
        SpecsFilter specsFilterTo = new SpecsFilter();
        Map<String, String> dateTo = new HashMap<>();
        dateTo.put("date", to);
        specsFilterTo.setFilterVal(dateTo);

        specsMap.put("sessionEndedFrom", specsFilterFrom);
        specsMap.put("sessionEndedTo", specsFilterTo);

        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartmentForReport(2L).and(hasSpecs(specsMap));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        assertThat("Set of Students Results is not of right size", results, hasSize(7));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllOfDepartmentAndCourseForReportBetweenDatesTest() {
        // Given: all depId=2 results, courseId = 1, between dates "2019-01-01" and "2020-01-01"
        Map<String, SpecsFilter> specsMap = new HashMap<>();

        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("2");
        specsMap.put("course", specsFilter);

        String from = "2019-01-01";
        SpecsFilter specsFilterFrom = new SpecsFilter();
        Map<String, String> dateFrom = new HashMap<>();
        dateFrom.put("date", from);
        specsFilterFrom.setFilterVal(dateFrom);

        String to = "2020-01-01";
        SpecsFilter specsFilterTo = new SpecsFilter();
        Map<String, String> dateTo = new HashMap<>();
        dateTo.put("date", to);
        specsFilterTo.setFilterVal(dateTo);

        specsMap.put("sessionEndedFrom", specsFilterFrom);
        specsMap.put("sessionEndedTo", specsFilterTo);

        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartmentForReport(2L).and(hasSpecs(specsMap));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        assertThat("Set of Students Results is not of right size", results, hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllOfDepartmentAndSchemeForReportBetweenDatesTest() {
        // Given: all depId=2 results, schemeId = 4, between dates "2019-01-01" and "2020-01-01"
        Map<String, SpecsFilter> specsMap = new HashMap<>();

        SpecsFilter specsFilter = new SpecsFilter();
        specsFilter.setFilterVal("4");
        specsMap.put("scheme", specsFilter);

        String from = "2019-01-01";
        SpecsFilter specsFilterFrom = new SpecsFilter();
        Map<String, String> dateFrom = new HashMap<>();
        dateFrom.put("date", from);
        specsFilterFrom.setFilterVal(dateFrom);

        String to = "2020-01-01";
        SpecsFilter specsFilterTo = new SpecsFilter();
        Map<String, String> dateTo = new HashMap<>();
        dateTo.put("date", to);
        specsFilterTo.setFilterVal(dateTo);

        specsMap.put("sessionEndedFrom", specsFilterFrom);
        specsMap.put("sessionEndedTo", specsFilterTo);

        Specification<ResultOfStudent> specs = ResultOfStudentStaffSpecs.ofDepartmentForReport(2L).and(hasSpecs(specsMap));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        assertThat("Set of Students Results is not of right size", results, hasSize(2));
    }

}
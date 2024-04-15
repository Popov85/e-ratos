package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.Student;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class StudentRepositoryTestIT {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private PersistenceUnitUtil persistenceUnitUtil;

    @BeforeEach
    public void setUp() {
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    @Test
    @Sql(scripts = "/scripts/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForAuthenticationTest() {
        Optional<Student> optional = studentRepository.findByIdForAuthentication("maria.medvedeva@example.com");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get(), "user"), "User of Student is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(optional.get().getUser(), "roles"), "Roles of User is not loaded");
        assertThat("Student is not as expected", optional.get(), allOf(
                hasProperty("studId", equalTo(2L)),
                hasProperty("user", allOf(
                        hasProperty("name", equalTo("Maria")),
                        hasProperty("surname", equalTo("Medvedeva")),
                        hasProperty("email", equalTo("maria.medvedeva@example.com"))
                ))
        ));
    }

    //-------------------------------------------------------------------------------------------------------------------
    @Test
    @Sql(scripts = "/scripts/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForEditTest() {
        Optional<Student> stud = studentRepository.findOneForEdit(2L);
        assertTrue(stud.isPresent(), "Student object is not found");
        assertTrue(persistenceUnitUtil.isLoaded(stud.get(), "user"), "User of Student is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(stud.get(), "studentClass"), "StudentClass of Student is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(stud.get().getStudentClass(), "faculty"), "Faculty of StudentClass is not loaded");
        assertTrue(persistenceUnitUtil.isLoaded(stud.get().getStudentClass(), "organisation"), "Organisation of StudentClass is not loaded");
        assertThat("Student is not as expected", stud.get(), allOf(
                hasProperty("studId", equalTo(2L)),
                hasProperty("user", allOf(
                        hasProperty("name", equalTo("Maria")),
                        hasProperty("surname", equalTo("Medvedeva")),
                        hasProperty("email", equalTo("maria.medvedeva@example.com"))
                ))
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/student_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByOrgIdTest() {
        Page<Student> page = studentRepository.findAllByOrgId(1L, PageRequest.of(0, 50));
        assertThat("Page of Courses is not as expected", page, allOf(
                hasProperty("content", hasSize(4)),
                hasProperty("totalPages", equalTo(1)),
                hasProperty("totalElements", equalTo(4L))));
        page.getContent()
                .forEach(s -> {
                    assertTrue(persistenceUnitUtil.isLoaded(s, "user"), "User of Student is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s, "studentClass"), "StudentClass of Student is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s.getStudentClass(), "faculty"), "Faculty of StudentClass is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s.getStudentClass(), "organisation"), "Organisation of StudentClass is not loaded");
                });
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/student_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByOrgIdAndSurnameLettersContainsTest() {
        Slice<Student> slice = studentRepository.findAllByOrgIdAndNameLettersContains(2L, "son", PageRequest.of(0, 50));
        assertThat("Slice of Courses is not as expected", slice, allOf(
                hasProperty("number", equalTo(0)),
                hasProperty("size", equalTo(50)),
                hasProperty("numberOfElements", equalTo(2)),
                hasProperty("content", hasSize(2)),
                hasProperty("first", equalTo(true)),
                hasProperty("last", equalTo(true))
        ));
        slice.getContent()
                .forEach(s -> {
                    assertTrue(persistenceUnitUtil.isLoaded(s, "user"), "User of Student is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s, "studentClass"), "StudentClass of Student is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s.getStudentClass(), "faculty"), "Faculty of StudentClass is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s.getStudentClass(), "organisation"), "Organisation of StudentClass is not loaded");
                });
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/student_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByOrgIdAndEmailLettersContainsTest() {
        Slice<Student> slice = studentRepository.findAllByOrgIdAndNameLettersContains(2L, "com", PageRequest.of(0, 50));
        assertThat("Slice of Courses is not as expected", slice, allOf(
                hasProperty("number", equalTo(0)),
                hasProperty("size", equalTo(50)),
                hasProperty("numberOfElements", equalTo(4)),
                hasProperty("content", hasSize(4)),
                hasProperty("first", equalTo(true)),
                hasProperty("last", equalTo(true))
        ));
        slice.getContent()
                .forEach(s -> {
                    assertTrue(persistenceUnitUtil.isLoaded(s, "user"), "User of Student is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s, "studentClass"), "StudentClass of Student is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s.getStudentClass(), "faculty"), "Faculty of StudentClass is not loaded");
                    assertTrue(persistenceUnitUtil.isLoaded(s.getStudentClass(), "organisation"), "Organisation of StudentClass is not loaded");
                });
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/student_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByOrgIdAndNameLettersContainsNegativeTest() {
        Slice<Student> slice = studentRepository.findAllByOrgIdAndNameLettersContains(2L, "fr", PageRequest.of(0, 50));
        assertThat("Slice of Courses is not as expected", slice, allOf(
                hasProperty("number", equalTo(0)),
                hasProperty("size", equalTo(50)),
                hasProperty("numberOfElements", equalTo(0)),
                hasProperty("content", empty()),
                hasProperty("first", equalTo(true)),
                hasProperty("last", equalTo(true))
        ));
    }
}

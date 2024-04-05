package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.Help;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class HelpRepositoryTestIT {

    @Autowired
    private HelpRepository helpRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/help_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForUpdateTest() {
        Optional<Help> optional = helpRepository.findOneForUpdate(1L);
        assertTrue(optional.isPresent(), "Help was not found with helpId = 1L");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/help_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByStaffIdTest() {
        assertThat("Page of Help is not of size = 3",
                helpRepository.findAllByStaffId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/help_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Page of Help is not of size = 3",
                helpRepository.findAllByDepartmentId(1L, PageRequest.of(0, 50)).getContent(), hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/help_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByStaffIdAndNameLettersContainsTest() {
        assertThat("Page of Help is not of size = 2",
                helpRepository.findAllByStaffIdAndNameLettersContains(1L, "assist", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/help_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdAndNameLettersContainsTest() {
        assertThat("Page of Help is not of size = 2",
                helpRepository.findAllByDepartmentIdAndNameLettersContains(1L, "assist", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/help_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of Help is not of size = 7",
                helpRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(7));
    }

}

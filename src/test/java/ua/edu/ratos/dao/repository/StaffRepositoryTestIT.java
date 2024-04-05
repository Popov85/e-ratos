package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class StaffRepositoryTestIT {

    @Autowired
    private StaffRepository staffRepository;

    @Test
    @Sql(scripts = "/scripts/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForAuthenticationTest() {
        assertTrue(staffRepository.findByIdForAuthentication("alexei.portnov@example.com").isPresent(), "Staff is not found");
    }

    @Test
    @Sql(scripts = "/scripts/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForEditTest() {
        assertTrue(staffRepository.findOneForEdit(1L).isPresent(), "Staff is not found");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/staff_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdSetTest() {
        assertThat("Page of Staff is not of size = 4",
                staffRepository.findAllByDepartmentId(1L), hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/staff_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Page of Staff is not of size = 4",
                staffRepository.findAllByDepartmentId(1L, PageRequest.of(0, 50)).getContent(), hasSize(4));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/staff_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdAndNameLettersContainsTest() {
        assertThat("Page of Staff is not of size = 2",
                staffRepository.findAllByDepartmentIdAndNameLettersContains(1L, "mm", PageRequest.of(0, 50)).getContent(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/staff_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdAndNameLettersContainsNegativeTest() {
        assertThat("Page of Staff is not empty",
                staffRepository.findAllByDepartmentIdAndNameLettersContains(1L, "ss", PageRequest.of(0, 50)).getContent(), empty());
    }
}

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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ModeRepositoryTestIT {

    @Autowired
    private ModeRepository modeRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/mode_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForEditTest() {
        assertTrue(modeRepository.findOneForEdit(1L).isPresent(), "Mode was not found for modeId = 1L");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/mode_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllDefaultTest() {
        assertThat("Set of default Mode is not of size = 3", modeRepository.findAllDefault(), hasSize(3));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/mode_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllBDepartmentIdTest() {
        assertThat("Set of Mode by department 1L is not of size = 2", modeRepository.findAllByDepartmentId(1L), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/mode_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of Mode is not of size = 9", modeRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(9));
    }
}

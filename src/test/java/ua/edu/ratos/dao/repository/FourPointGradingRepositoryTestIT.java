package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.grading.FourPointGrading;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class FourPointGradingRepositoryTestIT {

    @Autowired
    private FourPointGradingRepository fourPointGradingRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/four_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOneForEditTest() {
        Optional<FourPointGrading> optional = fourPointGradingRepository.findOneForEdit(1L);
        assertTrue(optional.isPresent(), "FourPointGrading was not found with fourId = 1L");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/four_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of FourPointGrading is not of size = 9",
                fourPointGradingRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(9));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/four_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllDefaultTest() {
        assertThat("Set of default FourPointGrading is not of size = 1",
                fourPointGradingRepository.findAllDefault(), hasSize(1));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/four_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Set of FourPointGrading is not of size = 4",
                fourPointGradingRepository.findAllByDepartmentId(2L), hasSize(4));
    }
}

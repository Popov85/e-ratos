package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.grading.FreePointGrading;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FreePointGradingRepositoryTestIT extends BaseIT {

    @Autowired
    private FreePointGradingRepository freePointGradingRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/free_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findOneForEditTest() {
        Optional<FreePointGrading> optional = freePointGradingRepository.findOneForEdit(1L);
        assertTrue(optional.isPresent(), "FreePointGrading was not found with freeId = 1L");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/free_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of FreePointGrading is not of size = 10",
                freePointGradingRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(10));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/free_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllDefaultTest() {
        assertThat("Set of default FreePointGrading is not of size = 2",
                freePointGradingRepository.findAllDefault(), hasSize(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/free_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Slice of FreePointGrading is not of size = 4",
                freePointGradingRepository.findAllByDepartmentId(2L), hasSize(4));
    }

}

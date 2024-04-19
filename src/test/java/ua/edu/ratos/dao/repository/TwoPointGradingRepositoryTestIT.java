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
import ua.edu.ratos.dao.entity.grading.TwoPointGrading;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TwoPointGradingRepositoryTestIT extends BaseIT {

    @Autowired
    private TwoPointGradingRepository twoPointGradingRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/two_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findOneForEditTest() {
        Optional<TwoPointGrading> optional = twoPointGradingRepository.findOneForEdit(1L);
        assertTrue(optional.isPresent(), "TwoPointGrading was not found with twoId = 1L");
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/two_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllTest() {
        assertThat("Page of TwoPointGrading is not of size = 9",
                twoPointGradingRepository.findAll(PageRequest.of(0, 50)).getContent(), hasSize(9));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/two_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllDefaultTest() {
        assertThat("Set of default TwoPointGrading is not of size = 1",
                twoPointGradingRepository.findAllDefault(), hasSize(1));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/two_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDepartmentIdTest() {
        assertThat("Set of TwoPointGrading is not of size = 4",
                twoPointGradingRepository.findAllByDepartmentId(2L), hasSize(4));
    }
}

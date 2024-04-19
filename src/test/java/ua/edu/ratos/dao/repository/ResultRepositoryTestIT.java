package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Result;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;


@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ResultRepositoryTestIT extends BaseIT {

    @Autowired
    private ResultRepository resultRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findFirstByUserIdAndSchemeIdTest() {
        Slice<Result> slice = resultRepository.findFirstByUserIdAndSchemeId(3L, 5L, PageRequest.of(0, 1));
        assertThat("Slice of Result is not as expected", slice, allOf(
                hasProperty("content", hasSize(1)),
                hasProperty("last", equalTo(true)),
                hasProperty("first", equalTo(true))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findFirstByUserIdAndSchemeIdNegativeTest() {
        Slice<Result> slice = resultRepository.findFirstByUserIdAndSchemeId(22L, 99L, PageRequest.of(0, 1));
        assertThat("Slice of Result is not as expected", slice, allOf(
                hasProperty("content", empty()),
                hasProperty("last", equalTo(true)),
                hasProperty("first", equalTo(true))));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findLatestTakenSchemesIdsTest() {
        Slice<Long> slice = resultRepository.findLatestTakenSchemesIds(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "resultId")));
        assertThat("Long list is not of size = 5", slice.getContent(), hasSize(5));
    }
}

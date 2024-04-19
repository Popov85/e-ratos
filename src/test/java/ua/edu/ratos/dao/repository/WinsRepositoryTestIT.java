package ua.edu.ratos.dao.repository;

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
import ua.edu.ratos.dao.entity.game.Wins;
import ua.edu.ratos.dao.repository.game.WinsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WinsRepositoryTestIT extends BaseIT {

    @Autowired
    private WinsRepository winsRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/wins_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllWinnersSinceTest() {
        // Given: 10 students, 3 winners of last week, many other winner for previous weeks,
        // It's now Wednesday and I want to know who won last week (Sunday 2019-02-24)?
        LocalDate localDate = LocalDate.parse("2019-02-27");
        // Who won last week?
        Page<Wins> result = winsRepository.findAllWinnersSince(localDate.minusDays(7), PageRequest.of(0, 10));
        assertThat("Page of Wins is not of size = 3", result.getContent(), hasSize(3));
        // Winners should be students with IDs {2, 3, 4}
        List<Long> winnersLastWeek = result.getContent()
                .stream()
                .map(w -> w.getStudent().getStudId())
                .collect(Collectors.toList());
        assertThat("Page of winners userIds does not contain all required IDs",
                winnersLastWeek, containsInAnyOrder(2L, 3L, 4L));
    }
}

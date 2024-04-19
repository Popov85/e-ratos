package ua.edu.ratos.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.game.Game;
import ua.edu.ratos.dao.repository.game.GameRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryTestIT extends BaseIT {

    @Autowired
    private GameRepository gameRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findOneForEditTest() {
        //Given Game entry for userId = 2L with totalWins = 1 (see sql script)
        gameRepository.incrementWins(2L);
        Optional<Game> game = gameRepository.findById(2L);
        assertTrue(game.isPresent(), "Gamer is not found");
        assertThat("TotalWins is not incremented to 2", game.get().getTotalWins(), equalTo(2));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findBestGamersTest() {
        Page<Game> page = gameRepository.findBestGamers(PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "totalPoints")));
        assertThat("Best gamers page is not of size = 2", page.getContent(), hasSize(2));
    }

}

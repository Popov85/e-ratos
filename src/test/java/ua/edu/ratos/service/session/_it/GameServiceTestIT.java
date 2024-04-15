package ua.edu.ratos.service.session._it;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.game.Game;
import ua.edu.ratos.dao.entity.game.Week;
import ua.edu.ratos.dao.entity.game.Wins;
import ua.edu.ratos.service.domain.SchemeDomain;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.session.GameService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GameServiceTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GameService gameService;

    @MockBean
    private SessionData sessionData;


    //-------------------------------------------------------Get points-------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPointsNotEnoughPercentsTest() {
        when(sessionData.getUserId()).thenReturn(2L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));
        // Actual test begins
        Optional<Integer> integer = gameService.getPoints(sessionData, 79.5);
        assertThat("Gamification points for 79.5% are not equal to 0",
                integer.orElseThrow(() -> new AssertionError("Value not present")), equalTo(0));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_one.sql", "/scripts/game_results_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPointsNotFirstAttemptTest() {
        when(sessionData.getUserId()).thenReturn(2L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));
        // Actual test begins
        Optional<Integer> integer = gameService.getPoints(sessionData, 85.6);
        assertThat("Gamification points for secondary attempts are not equal to 0",
                integer.orElseThrow(() -> new AssertionError("Value not present")), equalTo(0));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPointsHighestPercentsTest() {
        when(sessionData.getUserId()).thenReturn(2L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));
        // Actual test begins
        Optional<Integer> integer = gameService.getPoints(sessionData, 99.9);
        assertThat("Gamification points for 99.9% are not equal to 5 (max)",
                integer.orElseThrow(() -> new AssertionError("Value not present")), equalTo(5));
    }


    //-------------------------------------------------------Save points------------------------------------------------
    @Test
    @Sql(scripts = {"/scripts/init.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void doGameProcessingCreateTest() {
        // Given: userId = 2L scored enough for being granted 5 points (99%) for 781 secs first time ever
        // no entries for this user in week/game tables before
        when(sessionData.getUserId()).thenReturn(2L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));

        // Actual test begins
        gameService.doGameProcessing(sessionData, 99, 781);
        // expected entry in week table, expected entry in game table
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId=:studId").setParameter("studId", 2L).getSingleResult();
        assertThat("Week object is not as expected", week, allOf(
                hasProperty("weekPoints", equalTo(5)),
                hasProperty("weekBonuses", equalTo(0)),
                hasProperty("weekStrike", equalTo(1)),
                hasProperty("weekTimeSpent", equalTo(781L))
        ));
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 2L).getSingleResult();
        assertThat("Game object is not as expected", game, allOf(
                hasProperty("totalPoints", equalTo(5)),
                hasProperty("totalWins", equalTo(0)),
                hasProperty("totalBonuses", equalTo(0))
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_one.sql", "/scripts/week_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void doGameProcessingUpdateTest() {
        // Given: userId = 2L scored enough for being granted 3 points (92%) for 164 secs next time in the week
        // there were entries for this user in week (10 points, strike 1, time 2036)/game (100 points) tables before(!)
        when(sessionData.getUserId()).thenReturn(2L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));

        // Actual test begins
        gameService.doGameProcessing(sessionData, 92, 164);
        // expected updated entries in week table and in game table
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId=:studId").setParameter("studId", 2L).getSingleResult();
        assertThat("Week object is not as expected", week, allOf(
                hasProperty("weekPoints", equalTo(13)),
                hasProperty("weekBonuses", equalTo(0)),
                hasProperty("weekStrike", equalTo(2)),
                hasProperty("weekTimeSpent", equalTo(2200L))
        ));
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 2L).getSingleResult();
        assertThat("Game object is not as expected", game, allOf(
                hasProperty("totalPoints", equalTo(103)),
                hasProperty("totalWins", equalTo(1)),
                hasProperty("totalBonuses", equalTo(20))
        ));
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_one.sql", "/scripts/week_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void doGameProcessingStrikeTest() {
        // Given: userId = 3L scored enough for being granted points 3d time in the row this week (strike)
        // there were entries for this user in week(20 points, strike 2, bonuses 10, time 4150)/game (100 points, 20 bonuses)  tables before(!)
        when(sessionData.getUserId()).thenReturn(3L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));

        // Actual test begins
        gameService.doGameProcessing(sessionData, 80, 440);
        // expected updated entries in week table (+bonuses, points, reset strike) and in game table (+bonuses and points)
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId=:studId").setParameter("studId", 3L).getSingleResult();
        assertThat("Week object is not as expected", week, allOf(
                hasProperty("weekPoints", equalTo(21)),
                hasProperty("weekBonuses", equalTo(20)),//+10
                hasProperty("weekStrike", equalTo(0)),
                hasProperty("weekTimeSpent", equalTo(4590L))//+440
        ));
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 3L).getSingleResult();
        assertThat("Game object is not as expected", game, allOf(
                hasProperty("totalPoints", equalTo(101)),//+1
                hasProperty("totalWins", equalTo(1)),
                hasProperty("totalBonuses", equalTo(30))//+10
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/game_test_data_one.sql", "/scripts/week_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void doGameProcessingLostStrikeTest() {
        // Given: userId = 3L scored NOT enough for being granted any points.
        // there were entries for this user in week(20 points, strike 2, bonuses 10, time 4150)/game (100 points, 20 bonuses)  tables before(!)
        when(sessionData.getUserId()).thenReturn(3L);
        when(sessionData.isGameableSession()).thenReturn(true);
        when(sessionData.getSchemeDomain()).thenReturn(new SchemeDomain().setSchemeId(1L));

        //Actual test begins
        gameService.doGameProcessing(sessionData, 40, 440);
        // expected updated entries in week table (+bonuses, points, reset strike) and in game table (+bonuses and points)
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId=:studId").setParameter("studId", 3L).getSingleResult();
        assertThat("Week object is not as expected", week, allOf(
                hasProperty("weekPoints", equalTo(20)),
                hasProperty("weekBonuses", equalTo(10)),
                hasProperty("weekStrike", equalTo(0)),
                hasProperty("weekTimeSpent", equalTo(4590L))//+440
        ));
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 3L).getSingleResult();
        assertThat("Game object is not as expected", game, allOf(
                hasProperty("totalPoints", equalTo(100)),
                hasProperty("totalWins", equalTo(1)),
                hasProperty("totalBonuses", equalTo(20))
        ));
    }


    //-----------------------------------------------Quartz regular weekly job------------------------------------------
    @Test
    @SuppressWarnings("unchecked")
    @Sql(scripts = {"/scripts/init.sql", "/scripts/week_test_data_many.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void calculateAndSaveWeeklyWinnersFirstWeekTest() {
        // Actual test begins
        gameService.calculateAndSaveWeeklyWinners();
        // TOP-3 weekly winners
        List<Wins> result = (List<Wins>) em.createQuery("select w from Wins w").getResultList();
        assertThat("Weekly winners list size is not 3", result.size(), equalTo(3));
        List<Long> winners = result.stream().map(w -> w.getStudent().getStudId()).collect(Collectors.toList());
        assertThat("Weekly winners are not those who have ID's 9, 11, 18", winners, containsInAnyOrder(9L, 11L, 18L));
        // check updates of game's totalWins for these 3 winners
        final Game game9 = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 9L).getSingleResult();
        final Game game11 = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 11L).getSingleResult();
        final Game game18 = (Game) em.createQuery("select g from Game g where g.gameId=:studId").setParameter("studId", 18L).getSingleResult();
        assertThat("Total wins for user Id = 9 is not increased", game9.getTotalWins(), equalTo(1)); // +1
        assertThat("Total wins for user Id = 11 is not increased", game11.getTotalWins(), equalTo(1)); // +1
        assertThat("Total wins for user Id = 18 is not increased", game18.getTotalWins(), equalTo(1)); // +1
        // Make sure the week table was emptied
        List<Week> emptyWeek = em.createQuery("select w from Week w").getResultList();
        assertThat("Week table was not emptied", emptyWeek.isEmpty(), is(true));
    }
}

package _functional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.RatosApplication;
import ua.edu.ratos._helper.ResponseGeneratorSQHelper;
import ua.edu.ratos.dao.entity.Result;
import ua.edu.ratos.dao.entity.ResultDetails;
import ua.edu.ratos.dao.entity.ResultTheme;
import ua.edu.ratos.dao.entity.ResultThemeId;
import ua.edu.ratos.dao.entity.game.Game;
import ua.edu.ratos.dao.entity.game.Week;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.session.ResultOutDto;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.session.GenericSessionService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RatosApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StaticSQServiceTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    private ResponseGeneratorSQHelper responseGeneratorSQHelper = new ResponseGeneratorSQHelper();

    //--------------------------------------------------------not batched-----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_sq_scheme_non_dynamic_not_batched.sql", "/scripts/_functional/case_simple_sq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void nonDynamicCaseS1T1SQ10PerBatch1CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 SQ questions, all are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct questions
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            Map<Long, Response> response = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, questionId, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
        Map<Long, Response> response = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, questionId, true);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        assertEquals(5, resultOutDto.getPoints().intValue());
        assertNotNull(resultOutDto.getThemeResults());
        assertNotNull(resultOutDto.getQuestionResults());

        // Expected: new single entry in each: Result, ResultDetails, ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        ResultThemeId resultThemeId = new ResultThemeId(1L, 1L);
        final ResultTheme resultTheme = (ResultTheme) em.createQuery("select r from ResultTheme r where r.resultThemeId =:resultThemeId").setParameter("resultThemeId", resultThemeId).getSingleResult();
        assertNotNull(result);
        assertNotNull(resultDetails);
        assertNotNull(resultTheme);

        // Expected gamification changes: a new single entry in Week (points(5), strike(1), timeSpent(~0)), a new single entry in Game (points(5), bonuses (0), timeSpent(~0)),
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        assertNotNull(game);
    }

    //----------------------------------------------------------batched 6------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_sq_scheme_non_dynamic_batched_6.sql", "/scripts/_functional/case_simple_sq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void nonDynamicCaseS1T1SQ10PerBatch6Correct4Test() {
        /*
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 SQ questions, all are requested;
         * 6 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: Only 4 correct answers
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            Long question2Id = currentBatch.getQuestions().get(1).getQuestionId();
            Map<Long, Response> response = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, questionId, true);
            Map<Long, Response> response2 = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, question2Id, true);
            response.putAll(response2);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        // Create a correct response!
        Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
        Long question2Id = currentBatch.getQuestions().get(1).getQuestionId();
        Map<Long, Response> response = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, questionId, true);
        Map<Long, Response> response2 = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, question2Id, true);
        response.putAll(response2);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertFalse(resultOutDto.isPassed());
        assertEquals("40", resultOutDto.getPercent());
        assertEquals("2", resultOutDto.getGrade());
        assertEquals(0, resultOutDto.getPoints().intValue());
        assertNotNull(resultOutDto.getThemeResults());
        assertNotNull(resultOutDto.getQuestionResults());

        // Expected: new single entry in each: Result, ResultDetails, ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        ResultThemeId resultThemeId = new ResultThemeId(1L, 1L);
        final ResultTheme resultTheme = (ResultTheme) em.createQuery("select r from ResultTheme r where r.resultThemeId =:resultThemeId").setParameter("resultThemeId", resultThemeId).getSingleResult();
        assertNotNull(result);
        assertNotNull(resultDetails);
        assertNotNull(resultTheme);

        // Expected gamification changes: a new single entry in Week (points(5), strike(1), timeSpent(~0)), a new single entry in Game (points(5), bonuses (0), timeSpent(~0)),
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }
}

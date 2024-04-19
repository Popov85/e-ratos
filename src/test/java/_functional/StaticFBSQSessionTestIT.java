package _functional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.RatosApplication;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos._helper.ResponseGeneratorFBSQHelper;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RatosApplication.class)
@Import(TestContainerConfig.class)
public class StaticFBSQSessionTestIT extends BaseIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    private final ResponseGeneratorFBSQHelper responseGeneratorFBSQHelper = new ResponseGeneratorFBSQHelper();

    //---------------------------------------------------------not batched----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_fbsq_scheme_non_dynamic_not_batched.sql", "/scripts/_functional/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1FBSQ10PerBatch1Incorrect2Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 2 incorrect questions, {3L, 8L}
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            Map<Long, Response> response;
            if (questionId.equals(3L) || questionId.equals(8L)) {
                response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, questionId, false);
            } else {
                response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, questionId, true);
            }
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        Map<Long, Response> response;
        Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
        if (questionId.equals(3L) || questionId.equals(8L)) {
            response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, questionId, false);
        } else {
            response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, questionId, true);
        }
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("80", resultOutDto.getPercent());
        assertEquals("4", resultOutDto.getGrade());
        assertEquals(1, resultOutDto.getPoints().intValue());
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

        // Expected gamification changes: a new single entry in Week
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        assertNotNull(week);
        assertNotNull(game);
    }

    //-----------------------------------------------------------batched 4----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_fbsq_scheme_non_dynamic_batched_4.sql", "/scripts/_functional/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1FBSQ10PerBatch4Incorrect3Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * 4 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 3 incorrect questions (single incorrect each of 3 batches)
         */

        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        int i = 0;
        while (!isLastBatch) {
            Map<Long, Response> response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, currentBatch, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
            i++;
        }
        assertEquals(2, i);
        // Here you show the last question and collect the last response
        // Create a correct response!
        Map<Long, Response> response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, currentBatch, true);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("70", resultOutDto.getPercent()); // game starts from 80
        assertEquals("4", resultOutDto.getGrade());
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

        // Expected gamification changes: a new single entry in Week
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }

}

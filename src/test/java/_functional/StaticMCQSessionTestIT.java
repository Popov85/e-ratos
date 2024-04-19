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
import ua.edu.ratos._helper.ResponseGeneratorMCQHelper;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RatosApplication.class)
@Import(TestContainerConfig.class)
public class StaticMCQSessionTestIT extends BaseIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    private final ResponseGeneratorMCQHelper responseGeneratorMCQHelper = new ResponseGeneratorMCQHelper();

    //-----------------------------------------------------not batched--------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_not_batched.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch1IncorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: no correct answers (empty responses) for any of 10 questions.
         */
        SessionData sessionData = genericSessionService.start(1L);
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        int i = 0;
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
            // Client clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
            i++;
        }
        assertEquals(9, i);
        // Here you show the last question and collect the last response(empty!)
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(new HashMap<>()), sessionData);

        // Expected result:
        assertFalse(resultOutDto.isPassed());
        assertEquals("0", resultOutDto.getPercent());
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

        // Expected gamification changes: a single entry in Week, updated timeSpent
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_not_batched.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch1CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        // Create a correct response!
        Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
        Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
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
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        assertNotNull(week);
        assertNotNull(game);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_not_batched.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch1Incorrect2Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: majority correct answers for each out of 10 questions, 2 incorrect responses: {2L, 9L};
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            Map<Long, Response> response;
            if (questionId.equals(2L) || questionId.equals(9L)) {
                response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, false);
            } else {
                response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
            }
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
        }
        ;
        // Here you show the last question and collect the last response
        Map<Long, Response> response;
        // Create a correct response!
        Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
        if (questionId.equals(2L) || questionId.equals(9L)) {
            response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, false);
        } else {
            response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
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

        // Expected gamification changes: a new single entry in Week (points(5), strike(1), timeSpent(~0)), a new single entry in Game (points(5), bonuses (0), timeSpent(~0)),
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        assertNotNull(week);
        assertNotNull(game);
    }

    //-----------------------------------------------------Batched all--------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_all.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatchAllIncorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * All questions per batch, non-dynamic implementation (you launch finish just after first batch);
         * Non-LMS environment;
         * Scenario: no correct answers (empty responses) for all of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        ResultOutDto resultOutDto = null;
        // Here you show the last question and collect the last response(empty!)
        if (isLastBatch) {
            resultOutDto = genericSessionService.finish(new BatchInDto(new HashMap<>()), sessionData);
        }
        // Expected result:
        assertFalse(resultOutDto.isPassed());
        assertEquals("0", resultOutDto.getPercent());
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

        // Expected gamification changes: a single entry in Week, updated timeSpent
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_all.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatchAllCorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * All questions per batch, non-dynamic implementation (you launch finish just after first batch);
         * Non-LMS environment;
         * Scenario: all are correct answers for every out of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        ResultOutDto resultOutDto = null;
        // Here you show the last question and collect the last response(empty!)
        if (isLastBatch) {
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, null);
            resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);
        }
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

        // Expected gamification changes: a single entry in Week, updated timeSpent
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_all.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatchAllIncorrect1Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * All questions per batch, non-dynamic implementation (you launch finish just after first batch);
         * Non-LMS environment;
         * Scenario: all are correct answers except one, incorrect questionId = {5L}
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        ResultOutDto resultOutDto = null;
        // Here you show the last question and collect the last response(empty!)
        if (isLastBatch) {
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, 5L);
            resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);
        }
        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("90", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        assertEquals(3, resultOutDto.getPoints().intValue());
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

        // Expected gamification changes: a single entry in Week, updated timeSpent
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }


    //--------------------------------------------------Batched sessions 2 per sheet------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch2IncorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: no correct answers (empty responses) for any of 10 questions.
         */
        SessionData sessionData = genericSessionService.start(1L);
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        int i = 0;
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
            // Client clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
            i++;
        }
        assertEquals(4, i);
        // Here you show the last question and collect the last response(empty!)
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(new HashMap<>()), sessionData);

        // Expected result:
        assertFalse(resultOutDto.isPassed());
        assertEquals("0", resultOutDto.getPercent());
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

        // Expected gamification changes: a single entry in Week, updated timeSpent
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch2CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        // Create a correct response!
        Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
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
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        assertNotNull(week);
        assertNotNull(game);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch2Incorrect5Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 5 incorrect-s (1 per batch of 2 questions each) out of 10 in total
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        // Create a correct response!
        Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, true);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("50", resultOutDto.getPercent());
        assertEquals("3", resultOutDto.getGrade());
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

    //--------------------------------------------------Batched sessions 3 per sheet------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_3.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch3IncorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 3 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: no correct answers (empty responses) for any of 10 questions.
         */
        SessionData sessionData = genericSessionService.start(1L);
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        int i = 0;
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
            // Client clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
            i++;
        }
        assertEquals(3, i);
        // Here you show the last question and collect the last response(empty!)
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(new HashMap<>()), sessionData);

        // Expected result:
        assertFalse(resultOutDto.isPassed());
        assertEquals("0", resultOutDto.getPercent());
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

        // Expected gamification changes: a single entry in Week, updated timeSpent
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_3.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch3CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 3 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions.
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        int i = 0;
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
            i++;
        }
        assertEquals(3, i);
        // Here you show the last question and collect the last response
        // Create a correct response!
        Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
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
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        assertNotNull(week);
        assertNotNull(game);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_non_dynamic_batched_3.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void nonDynamicCaseS1T1MCQ10PerBatch3Incorrect3Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 3 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 3 (one per each first 3 batches, each per 3 questions) are incorrect out of 10 in total.
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        int i = 0;
        while (!isLastBatch) {
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            isLastBatch = currentBatch.isLastBatch();
            i++;
        }
        assertEquals(3, i);
        // Here you show the last question and collect the last response
        // Create a correct response!
        Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("70", resultOutDto.getPercent());
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

        // Expected gamification changes: a new single entry in Week (points(5), strike(1), timeSpent(~0)), a new single entry in Game (points(5), bonuses (0), timeSpent(~0)),
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        assertNotNull(week);
    }

}

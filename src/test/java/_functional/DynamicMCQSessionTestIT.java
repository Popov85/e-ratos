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
import ua.edu.ratos.service.domain.MetaData;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.session.ResultOutDto;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.session.EducationalSessionService;
import ua.edu.ratos.service.session.GenericSessionService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RatosApplication.class)
@Import(TestContainerConfig.class)
public class DynamicMCQSessionTestIT extends BaseIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    @Autowired
    private EducationalSessionService educationalSessionService;

    private final ResponseGeneratorMCQHelper responseGeneratorMCQHelper = new ResponseGeneratorMCQHelper();

    //--------------------------------------------------Not batched-----------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch1CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch1Skip1CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions, 1 skip performed questionId = {5L};
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int skipCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            if (skipCounter == 0 && questionId.equals(5L)) {
                educationalSessionService.skip(5L, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter++;
            } else {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);
        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        MetaData meta = metaData.get(5L);
        assertEquals(1, meta.getSkipped());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch1Incorrect1Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions, 1 incorrect, questionId = {5L};
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int incorrectCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            if (incorrectCounter == 0 && questionId.equals(5L)) {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                incorrectCounter++;
            } else {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, questionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        MetaData meta = metaData.get(5L);
        assertEquals(1, meta.getIncorrect());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }

    //------------------------------------------------Batched all-------------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_all.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatchAllCorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * All questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_all.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatchAllSkip3CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * All questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions, 3 skips performed questionId = {2L, 5L, 7L};
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int skipCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Skip 3 questions and for the rest of them create correct responses!
            if (skipCounter == 0) {
                educationalSessionService.skip(2L, sessionData);
                educationalSessionService.skip(5L, sessionData);
                educationalSessionService.skip(7L, sessionData);
                // Basically here we should sent empty responses for skipped questions!!!
                // Instead we send the correct answers that will not be counted as correct with this request.
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                skipCounter++;
            } else {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);
        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        MetaData meta1 = metaData.get(2L);
        MetaData meta2 = metaData.get(5L);
        MetaData meta3 = metaData.get(7L);
        assertEquals(1, meta1.getSkipped());
        assertEquals(1, meta2.getSkipped());
        assertEquals(1, meta3.getSkipped());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_all.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatchAllIncorrect3Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * All questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions, 3 incorrect-s;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int incorrectCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Skip 3 questions and for the rest of them create correct responses!
            if (incorrectCounter < 3) {
                // Basically here we should sent empty responses for skipped questions!!!
                // Instead we send the correct answers that will not be counted as correct with this request.
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                incorrectCounter++;
            } else {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);
        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        assertEquals(1, metaData.size());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }

    //---------------------------------------------------Batched 2------------------------------------------------------


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch2CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            batchCounter++;
        }
        assertEquals(5, batchCounter);
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch2Skip1CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions, 1 skip performed in 3-d batch;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 3) {
                // Perform skip in 3-d batch
                Long skippedQuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                Long answeredQuestionId = currentBatch.getQuestions().get(1).getQuestionId();
                educationalSessionService.skip(skippedQuestionId, sessionData);
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, answeredQuestionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            } else {
                // Create a correct response!
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
            batchCounter++;
        }
        assertEquals(6, batchCounter);
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        assertEquals(1, metaData.size());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch2SkipAllCorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions, skip all questions in 2-d batch;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 2) {
                // Perform skip in 2-d batch
                Long skipped1QuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                Long skipped2QuestionId = currentBatch.getQuestions().get(1).getQuestionId();
                educationalSessionService.skip(skipped1QuestionId, sessionData);
                educationalSessionService.skip(skipped2QuestionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
            } else {
                // Create a correct response!
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
            batchCounter++;
        }
        assertEquals(6, batchCounter);
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        assertEquals(2, metaData.size());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_2.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch2Skip1Incorrect1Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 2 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions except one, skip 1 in batch 2 and perform incorrect in batch 4;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 2) {
                // Perform skip in 2-d batch
                Long skipped1QuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                Long answeredQuestionId = currentBatch.getQuestions().get(1).getQuestionId();
                educationalSessionService.skip(skipped1QuestionId, sessionData);
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, answeredQuestionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            } else if (batchCounter == 4) {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            } else {
                // Create a correct response!
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
            batchCounter++;
        }
        assertEquals(6, batchCounter);
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        assertEquals(2, metaData.size());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }

    //----------------------------------------------------Batched 3-----------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_3.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch3CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 3 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            // Here you show question and collect response (empty!)
            // Create a correct response!
            Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            batchCounter++;
        }
        assertEquals(4, batchCounter);
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_mcq_scheme_dynamic_batched_3.sql", "/scripts/_functional/case_simple_mcq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dynamicCaseS1T1MCQ10PerBatch2Skip1Incorrect2Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 MCQ questions, all are requested;
         * 3 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all correct answers for each out of 10 questions except 2, skip 1 in batch 0 and perform incorrect in batch 0 and 1;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 0) {
                // Perform skip in 0-st batch
                Long skipped1QuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                Long answeredQuestionId = currentBatch.getQuestions().get(1).getQuestionId();
                Long answered2QuestionId = currentBatch.getQuestions().get(2).getQuestionId();
                educationalSessionService.skip(skipped1QuestionId, sessionData);
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, answeredQuestionId, false);
                Map<Long, Response> response2 = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, answered2QuestionId, true);
                response.putAll(response2);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            } else if (batchCounter == 1) {
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            } else {
                // Create a correct response!
                Map<Long, Response> response = responseGeneratorMCQHelper.getCorrectResponseMCQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
            batchCounter++;
        }
        assertEquals(5, batchCounter);
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertNull(resultOutDto.getPoints());
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
    }
}

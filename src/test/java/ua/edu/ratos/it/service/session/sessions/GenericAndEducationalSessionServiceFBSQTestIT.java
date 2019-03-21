package ua.edu.ratos.it.service.session.sessions;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ua.edu.ratos.dao.entity.Result;
import ua.edu.ratos.dao.entity.ResultDetails;
import ua.edu.ratos.dao.entity.ResultTheme;
import ua.edu.ratos.dao.entity.ResultThemeId;
import ua.edu.ratos.dao.entity.game.Game;
import ua.edu.ratos.dao.entity.game.Week;
import ua.edu.ratos.it.ActiveProfile;
import ua.edu.ratos.service.domain.MetaData;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.StartData;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.session.ResultOutDto;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.session.EducationalSessionService;
import ua.edu.ratos.service.session.GenericSessionService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenericAndEducationalSessionServiceFBSQTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SessionTestFBSQSupport sessionTestFBSQSupport;

    @Autowired
    private GenericSessionService genericSessionService;

    @Autowired
    private EducationalSessionService educationalSessionService;

    //-------------------------------------------------Non-Dynamic, not batched-----------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/session/case_simple_fbsq_scheme_non_dynamic_not_batched.sql", "/scripts/session/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void nonDynamicCaseS1T1FBSQ10PerBatch1Incorrect2Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 2 incorrect questions, {3L, 8L}
         */
        SessionData sessionData = genericSessionService.start(new StartData("123456", 1L, 2L));
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        log.debug("First batch = {}", currentBatch);
        int batchesLeft = currentBatch.getBatchesLeft();
        while (batchesLeft > 0) {
            Long questionId = currentBatch.getBatch().get(0).getQuestionId();
            Map<Long, Response> response;
            if (questionId.equals(3L) || questionId.equals(8L)) {
                response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, questionId, false);
            } else {
                response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, questionId, true);
            }
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            log.debug("Next batch = {}", currentBatch);
            batchesLeft = currentBatch.getBatchesLeft();
        }
        // Here you show the last question and collect the last response
        Map<Long, Response> response;
        log.debug("About to launch finish request...");
        Long questionId = currentBatch.getBatch().get(0).getQuestionId();
        if (questionId.equals(3L) || questionId.equals(8L)) {
            response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, questionId, false);
        } else {
            response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, questionId, true);
        }
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals(80.0, resultOutDto.getPercent().doubleValue(), 0.01);
        assertEquals(4.0, resultOutDto.getGrade().doubleValue(), 0.01);
        assertEquals(1, resultOutDto.getPoints().intValue());
        assertNull(resultOutDto.getThemeResults());
        assertNull(resultOutDto.getQuestionResults());

        // Expected: new single entry in each: Result, ResultDetails, ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        ResultThemeId resultThemeId = new ResultThemeId(1L, 1L);
        final ResultTheme resultTheme = (ResultTheme) em.createQuery("select r from ResultTheme r where r.resultThemeId =:resultThemeId").setParameter("resultThemeId", resultThemeId).getSingleResult();
        Assert.assertNotNull(result);
        Assert.assertNotNull(resultDetails);
        Assert.assertNotNull(resultTheme);

        // Expected gamification changes: a new single entry in Week (points(5), strike(1), timeSpent(~0)), a new single entry in Game (points(5), bonuses (0), timeSpent(~0)),
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        final Game game = (Game) em.createQuery("select g from Game g where g.gameId =:gameId").setParameter("gameId", 2L).getSingleResult();
        Assert.assertNotNull(week);
        Assert.assertNotNull(game);
        log.debug("Finished");
    }

    //-------------------------------------------------Non-Dynamic, batched 4-------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/session/case_simple_fbsq_scheme_non_dynamic_batched_4.sql", "/scripts/session/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void nonDynamicCaseS1T1FBSQ10PerBatch4Incorrect3Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * 4 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 3 incorrect questions (single incorrect each of 3 batches)
         */

        SessionData sessionData = genericSessionService.start(new StartData("123456", 1L, 2L));
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        log.debug("First batch = {}", currentBatch);
        int batchesLeft = currentBatch.getBatchesLeft();
        int i=0;
        while (batchesLeft > 0) {
            Map<Long, Response> response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, currentBatch, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            // Client just clicked button Next>>
            log.debug("Next batch = {}", currentBatch);
            batchesLeft = currentBatch.getBatchesLeft();
            i++;
        }
        assertEquals(2, i);
        // Here you show the last question and collect the last response
        log.debug("About to launch finish request...");
        // Create a correct response!
        Map<Long, Response> response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, currentBatch, true);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals(70.0, resultOutDto.getPercent().doubleValue(), 0.01);
        assertEquals(4.0, resultOutDto.getGrade().doubleValue(), 0.01);
        assertEquals(0, resultOutDto.getPoints().intValue());
        assertNull(resultOutDto.getThemeResults());
        assertNull(resultOutDto.getQuestionResults());

        // Expected: new single entry in each: Result, ResultDetails, ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        ResultThemeId resultThemeId = new ResultThemeId(1L, 1L);
        final ResultTheme resultTheme = (ResultTheme) em.createQuery("select r from ResultTheme r where r.resultThemeId =:resultThemeId").setParameter("resultThemeId", resultThemeId).getSingleResult();
        Assert.assertNotNull(result);
        Assert.assertNotNull(resultDetails);
        Assert.assertNotNull(resultTheme);

        // Expected gamification changes: a new single entry in Week (points(5), strike(1), timeSpent(~0)), a new single entry in Game (points(5), bonuses (0), timeSpent(~0)),
        final Week week = (Week) em.createQuery("select w from Week w where w.weekId =:weekId").setParameter("weekId", 2L).getSingleResult();
        Assert.assertNotNull(week);
        log.debug("Finished");
    }


    //-------------------------------------------------Dynamic, not batched---------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/session/case_simple_fbsq_scheme_dynamic_not_batched.sql", "/scripts/session/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1FBSQ10PerBatch1Skip2Incorrect1Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 2 skips {1L, 10L}, 1 incorrect question {5L};
         */
        SessionData sessionData = genericSessionService.start(new StartData("123456", 1L, 2L));
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        log.debug("First batch = {}", currentBatch);
        int skipCounter1 = 0;
        int skipCounter2 = 0;
        int incorrectCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            Long questionId = currentBatch.getBatch().get(0).getQuestionId();
            if (skipCounter1 == 0 && questionId.equals(1L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter1++;
            } if (skipCounter2 == 0 && questionId.equals(10L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter2++;
            } else if (incorrectCounter==0 && questionId.equals(5L)) {
                Map<Long, Response> response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, questionId, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                incorrectCounter++;
            } else {
                Map<Long, Response> response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, questionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
                log.debug("Next batch = {}", currentBatch);
            }
        }
        // Here you show the last question and collect the last response
        log.debug("About to launch finish request without evaluating last batch...");
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals(100.0, resultOutDto.getPercent().doubleValue(), 0.01);
        assertEquals(5.0, resultOutDto.getGrade().doubleValue(), 0.01);
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertEquals(0, resultOutDto.getPoints().intValue());
        assertNull(resultOutDto.getThemeResults());
        assertNull(resultOutDto.getQuestionResults());

        // Expected: new single entry in each: Result, ResultDetails, ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        ResultThemeId resultThemeId = new ResultThemeId(1L, 1L);
        final ResultTheme resultTheme = (ResultTheme) em.createQuery("select r from ResultTheme r where r.resultThemeId =:resultThemeId").setParameter("resultThemeId", resultThemeId).getSingleResult();
        Assert.assertNotNull(result);
        Assert.assertNotNull(resultDetails);
        Assert.assertNotNull(resultTheme);
        log.debug("Finished");
    }


    //-------------------------------------------------Dynamic, batched 9-----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/session/case_simple_fbsq_scheme_dynamic_batched_9.sql", "/scripts/session/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1FBSQ10PerBatch5Incorrect4Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * 9 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 1 skip, 4 incorrect questions (all in first big batch of 9 questions);
         */
        SessionData sessionData = genericSessionService.start(new StartData("123456", 1L, 2L));
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        log.debug("First batch = {}", currentBatch);
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 0) {
                // skipped
                Long skippedQuestionId = currentBatch.getBatch().get(0).getQuestionId();
                // incorrect
                Long incorrect1QuestionId = currentBatch.getBatch().get(1).getQuestionId();
                Long incorrect2QuestionId = currentBatch.getBatch().get(2).getQuestionId();
                Long incorrect3QuestionId = currentBatch.getBatch().get(3).getQuestionId();
                Long incorrect4QuestionId = currentBatch.getBatch().get(4).getQuestionId();

                // correct
                Long correct1QuestionId = currentBatch.getBatch().get(5).getQuestionId();
                Long correct2QuestionId = currentBatch.getBatch().get(6).getQuestionId();
                Long correct3QuestionId = currentBatch.getBatch().get(7).getQuestionId();
                Long correct4QuestionId = currentBatch.getBatch().get(8).getQuestionId();

                // perform single skip
                educationalSessionService.skip(skippedQuestionId, sessionData);
                // perform 4 incorrect answers
                Map<Long, Response> response = new HashMap<>();
                Map<Long, Response> response1 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, incorrect1QuestionId, false);
                Map<Long, Response> response2 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, incorrect2QuestionId, false);
                Map<Long, Response> response3 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, incorrect3QuestionId, false);
                Map<Long, Response> response4 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, incorrect4QuestionId, false);
                // perform 4 correct
                Map<Long, Response> response5 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, correct1QuestionId, true);
                Map<Long, Response> response6 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, correct2QuestionId, true);
                Map<Long, Response> response7 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, correct3QuestionId, true);
                Map<Long, Response> response8 = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, correct4QuestionId, true);

                response.putAll(response1);
                response.putAll(response2);
                response.putAll(response3);
                response.putAll(response4);

                response.putAll(response5);
                response.putAll(response6);
                response.putAll(response7);
                response.putAll(response8);

                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                batchCounter++;
            } else {
                Map<Long, Response> response = sessionTestFBSQSupport.getCorrectResponseFBSQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                log.debug("Next batch = {}", currentBatch);
            }
        }
        // Here you show the last question and collect the last response
        log.debug("About to launch finish request without evaluating last batch...");
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        assertEquals(5, metaData.size());

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals(100.0, resultOutDto.getPercent().doubleValue(), 0.01);
        assertEquals(5.0, resultOutDto.getGrade().doubleValue(), 0.01);
        // Gamification is disabled for educational sessions ({skips, pyramid, right answers})
        assertEquals(0, resultOutDto.getPoints().intValue());
        assertNull(resultOutDto.getThemeResults());
        assertNull(resultOutDto.getQuestionResults());

        // Expected: new single entry in each: Result, ResultDetails, ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        ResultThemeId resultThemeId = new ResultThemeId(1L, 1L);
        final ResultTheme resultTheme = (ResultTheme) em.createQuery("select r from ResultTheme r where r.resultThemeId =:resultThemeId").setParameter("resultThemeId", resultThemeId).getSingleResult();
        Assert.assertNotNull(result);
        Assert.assertNotNull(resultDetails);
        Assert.assertNotNull(resultTheme);
        log.debug("Finished");
    }
}

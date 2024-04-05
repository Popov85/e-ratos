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
import ua.edu.ratos._helper.ResponseGeneratorFBSQHelper;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RatosApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DynamicFBSQSessionTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    @Autowired
    private EducationalSessionService educationalSessionService;

    private ResponseGeneratorFBSQHelper responseGeneratorFBSQHelper = new ResponseGeneratorFBSQHelper();

    //--------------------------------------------------------not batched-----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_fbsq_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1FBSQ10PerBatch1Skip2Incorrect1Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 2 skips {1L, 10L}, 1 incorrect question {5L};
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int skipCounter1 = 0;
        int skipCounter2 = 0;
        int incorrectCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            if (skipCounter1 == 0 && questionId.equals(1L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter1++;
            }
            if (skipCounter2 == 0 && questionId.equals(10L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter2++;
            } else if (incorrectCounter == 0 && questionId.equals(5L)) {
                Map<Long, Response> response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, questionId, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                incorrectCounter++;
            } else {
                Map<Long, Response> response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, questionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            }
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


    //-------------------------------------------------------batched 9--------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_fbsq_scheme_dynamic_batched_9.sql", "/scripts/_functional/case_simple_fbsq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1FBSQ10PerBatch5Incorrect4Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBSQ questions, all are requested;
         * 9 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 1 skip, 4 incorrect questions (all in first big batch of 9 questions);
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 0) {
                // skipped
                Long skippedQuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                // incorrect
                Long incorrect1QuestionId = currentBatch.getQuestions().get(1).getQuestionId();
                Long incorrect2QuestionId = currentBatch.getQuestions().get(2).getQuestionId();
                Long incorrect3QuestionId = currentBatch.getQuestions().get(3).getQuestionId();
                Long incorrect4QuestionId = currentBatch.getQuestions().get(4).getQuestionId();

                // correct
                Long correct1QuestionId = currentBatch.getQuestions().get(5).getQuestionId();
                Long correct2QuestionId = currentBatch.getQuestions().get(6).getQuestionId();
                Long correct3QuestionId = currentBatch.getQuestions().get(7).getQuestionId();
                Long correct4QuestionId = currentBatch.getQuestions().get(8).getQuestionId();

                // perform single skip
                educationalSessionService.skip(skippedQuestionId, sessionData);
                // perform 4 incorrect answers
                Map<Long, Response> response = new HashMap<>();
                Map<Long, Response> response1 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, incorrect1QuestionId, false);
                Map<Long, Response> response2 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, incorrect2QuestionId, false);
                Map<Long, Response> response3 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, incorrect3QuestionId, false);
                Map<Long, Response> response4 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, incorrect4QuestionId, false);
                // perform 4 correct
                Map<Long, Response> response5 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, correct1QuestionId, true);
                Map<Long, Response> response6 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, correct2QuestionId, true);
                Map<Long, Response> response7 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, correct3QuestionId, true);
                Map<Long, Response> response8 = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, correct4QuestionId, true);

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
                Map<Long, Response> response = responseGeneratorFBSQHelper.getCorrectResponseFBSQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            }
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected sessionData state:
        Map<Long, MetaData> metaData = sessionData.getMetaData();
        assertEquals(5, metaData.size());

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

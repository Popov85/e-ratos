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
import ua.edu.ratos._helper.ResponseGeneratorFBMQHelper;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RatosApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DynamicFBMQSessionTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    @Autowired
    private EducationalSessionService educationalSessionService;

    private ResponseGeneratorFBMQHelper responseGeneratorFBMQHelper = new ResponseGeneratorFBMQHelper();

    //---------------------------------------------------------not batched----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_fbmq_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_simple_fbmq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1FBMQ10PerBatch1Skip2Incorrect2Test() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBMQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 2 skips {9L, 10L}, 2 incorrect question {5L, 6L};
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int skipCounter1 = 0;
        int skipCounter2 = 0;
        int incorrectCounter1 = 0;
        int incorrectCounter2 = 0;
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            if (skipCounter1 == 0 && questionId.equals(9L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter1++;
            }
            if (skipCounter2 == 0 && questionId.equals(10L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter2++;
            } else if (incorrectCounter1 == 0 && questionId.equals(5L)) {
                Map<Long, Response> response = responseGeneratorFBMQHelper.getCorrectResponseFBMQMap(questionsMap, questionId, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                incorrectCounter1++;
            } else if (incorrectCounter2 == 0 && questionId.equals(6L)) {
                Map<Long, Response> response = responseGeneratorFBMQHelper.getCorrectResponseFBMQMap(questionsMap, questionId, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                incorrectCounter2++;
            } else {
                Map<Long, Response> response = responseGeneratorFBMQHelper.getCorrectResponseFBMQMap(questionsMap, questionId, true);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            }
            batchCounter++;
        }
        assertEquals(14, batchCounter);
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


    //-------------------------------------------------Dynamic, batched 10-----------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_fbmq_scheme_dynamic_batched_10.sql", "/scripts/_functional/case_simple_fbmq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1FBMQ10PerBatch10CorrectAllTest() {
        /**
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 FBMQ questions, all are requested;
         * 10 questions per batch (all), dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: 5 skips
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 0) {
                // skipped
                Long skipped1QuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                Long skipped2QuestionId = currentBatch.getQuestions().get(1).getQuestionId();
                Long skipped3QuestionId = currentBatch.getQuestions().get(2).getQuestionId();
                Long skipped4QuestionId = currentBatch.getQuestions().get(3).getQuestionId();
                Long skipped5QuestionId = currentBatch.getQuestions().get(4).getQuestionId();

                educationalSessionService.skip(skipped1QuestionId, sessionData);
                educationalSessionService.skip(skipped2QuestionId, sessionData);
                educationalSessionService.skip(skipped3QuestionId, sessionData);
                educationalSessionService.skip(skipped4QuestionId, sessionData);
                educationalSessionService.skip(skipped5QuestionId, sessionData);

                Map<Long, Response> response = responseGeneratorFBMQHelper.getCorrectResponseFBMQMap(questionsMap, currentBatch, false);

                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                batchCounter++;
            } else {
                Map<Long, Response> response = responseGeneratorFBMQHelper.getCorrectResponseFBMQMap(questionsMap, currentBatch, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            }
        }
        // Here you show the last question and collect the last response.
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

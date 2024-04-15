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
import ua.edu.ratos._helper.ResponseGeneratorALLHelper;
import ua.edu.ratos.dao.entity.Result;
import ua.edu.ratos.dao.entity.ResultDetails;
import ua.edu.ratos.dao.entity.ResultTheme;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.session.ResultOutDto;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;
import ua.edu.ratos.service.session.EducationalSessionService;
import ua.edu.ratos.service.session.GenericSessionService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RatosApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DynamicALLSessionTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    @Autowired
    private EducationalSessionService educationalSessionService;

    private ResponseGeneratorALLHelper responseGeneratorALLHelper = new ResponseGeneratorALLHelper();


    //-------------------------------------------------------not batched------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_complex_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_complex_all.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void complexDynamicCaseS1T5Q50PerBatch1Incorrect4Test() {
        /*
         * UserId = 2L;
         * Complex case: 5 themes with questions per theme = {1, 5, 0, 5, 10}, total = 21 question
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-pyramidal! Incorrect-s are not returned back! Never!
         * Non-LMS environment;
         * Scenario: first 4 questions are incorrect
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int incorrectCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            QuestionSessionOutDto question = currentBatch.getQuestions().get(0);
            Long questionId = question.getQuestionId();
            long type = question.getType();
            Map<Long, Response> response;
            if (incorrectCounter <= 3) {
                response = responseGeneratorALLHelper.getResponseMap(type, questionsMap, questionId, false);
            } else {
                response = responseGeneratorALLHelper.getResponseMap(type, questionsMap, questionId, true);
            }
            incorrectCounter++;
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("81", resultOutDto.getPercent());
        assertEquals("4", resultOutDto.getGrade());
        // From session with right answers displayed - no gamification points can be granted!
        assertNull(resultOutDto.getPoints());
        assertEquals(4, resultOutDto.getThemeResults().size());
        assertEquals(21, resultOutDto.getQuestionResults().size());

        // Expected: new single entry in each: Result, ResultDetails, 5 entries in ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        assertNotNull(result);
        assertEquals(1L, result.getResultId().longValue());
        assertTrue(result.isPassed());
        assertFalse(result.isPoints());
        assertEquals(80.95, result.getPercent(), 0.1);
        assertEquals(4.0, result.getGrade(), 0.1);

        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        assertNotNull(resultDetails);
        assertEquals(1L, resultDetails.getDetailId().longValue());
        assertFalse(resultDetails.getJsonData().isEmpty());
        assertTrue(LocalDateTime.now().isAfter(resultDetails.getWhenRemove().minusHours(25)));

        final List<ResultTheme> resultThemes = (List<ResultTheme>) em.createQuery("select r from ResultTheme r join r.result rr where rr.resultId =:resultId").setParameter("resultId", 1L).getResultList();
        assertEquals(4, resultThemes.size());

        ResultTheme resultTheme1 = resultThemes.get(0);
        assertEquals(1L, resultTheme1.getTheme().getThemeId().longValue());
        assertEquals(1, resultTheme1.getQuantity());
        assertTrue(resultTheme1.getPercent() >= 0);

        ResultTheme resultTheme2 = resultThemes.get(1);
        assertEquals(2L, resultTheme2.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme2.getQuantity());
        assertTrue(resultTheme2.getPercent() >= 20);

        ResultTheme resultTheme4 = resultThemes.get(2);
        assertEquals(4L, resultTheme4.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme4.getQuantity());
        assertTrue(resultTheme4.getPercent() >= 20);

        ResultTheme resultTheme5 = resultThemes.get(3);
        assertEquals(5L, resultTheme5.getTheme().getThemeId().longValue());
        assertEquals(10, resultTheme5.getQuantity());
        assertTrue(resultTheme5.getPercent() >= 60);
    }


    //-------------------------------------------------------batched [4]------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_complex_scheme_dynamic_batched_4.sql", "/scripts/_functional/case_complex_all.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void complexDynamicCaseS1T5Q50PerBatch4Skip1Incorrect4Test() {
        /*
         * UserId = 2L;
         * Complex case: 5 themes {5, 5, 5, 5, 5}
         * 4 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-pyramidal! Incorrect-s are not returned back! Never!
         * Non-LMS environment;
         * Scenario: Single incorrect in first 4 batches out of 7 in total, one skip performed
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int incorrectCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            Map<Long, Response> response;
            if (incorrectCounter <= 3) {
                response = responseGeneratorALLHelper.getResponseMap(questionsMap, currentBatch, true);
            } else {
                if (incorrectCounter == 5) {
                    Long skippedId = currentBatch.getQuestions().get(0).getQuestionId();
                    educationalSessionService.skip(skippedId, sessionData);
                }
                response = responseGeneratorALLHelper.getResponseMap(questionsMap, currentBatch, false);
            }
            incorrectCounter++;
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("84", resultOutDto.getPercent());
        assertEquals("4", resultOutDto.getGrade());
        // From session with right answers displayed - no gamification points can be granted!
        assertNull(resultOutDto.getPoints());
        assertEquals(5, resultOutDto.getThemeResults().size());
        assertEquals(25, resultOutDto.getQuestionResults().size());

        // Expected: new single entry in each: Result, ResultDetails, 5 entries in ResultTheme
        final Result result = (Result) em.createQuery("select r from Result r where r.resultId =:resultId").setParameter("resultId", 1L).getSingleResult();
        assertNotNull(result);
        assertEquals(1L, result.getResultId().longValue());
        assertTrue(result.isPassed());
        assertFalse(result.isPoints());
        assertEquals(84.0, result.getPercent(), 0.1);
        assertEquals(4.0, result.getGrade(), 0.1);

        final ResultDetails resultDetails = (ResultDetails) em.createQuery("select r from ResultDetails r where r.detailId =:detailId").setParameter("detailId", 1L).getSingleResult();
        assertNotNull(resultDetails);
        assertEquals(1L, resultDetails.getDetailId().longValue());
        assertFalse(resultDetails.getJsonData().isEmpty());
        assertTrue(LocalDateTime.now().isAfter(resultDetails.getWhenRemove().minusHours(25)));

        final List<ResultTheme> resultThemes = (List<ResultTheme>) em.createQuery("select r from ResultTheme r join r.result rr where rr.resultId =:resultId").setParameter("resultId", 1L).getResultList();
        assertEquals(5, resultThemes.size());

        ResultTheme resultTheme1 = resultThemes.get(0);
        assertEquals(1L, resultTheme1.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme1.getQuantity());
        assertTrue(resultTheme1.getPercent() >= 20);

        ResultTheme resultTheme2 = resultThemes.get(1);
        assertEquals(2L, resultTheme2.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme2.getQuantity());
        assertTrue(resultTheme2.getPercent() >= 20);

        ResultTheme resultTheme3 = resultThemes.get(2);
        assertEquals(3L, resultTheme3.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme3.getQuantity());
        assertTrue(resultTheme3.getPercent() >= 20);

        ResultTheme resultTheme4 = resultThemes.get(3);
        assertEquals(4L, resultTheme4.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme4.getQuantity());
        assertTrue(resultTheme4.getPercent() >= 20);

        ResultTheme resultTheme5 = resultThemes.get(4);
        assertEquals(5L, resultTheme5.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme5.getQuantity());
        assertTrue(resultTheme5.getPercent() >= 20);
    }

}

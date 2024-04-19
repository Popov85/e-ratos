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
import ua.edu.ratos.service.session.GenericSessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RatosApplication.class)
@Import(TestContainerConfig.class)
public class StaticALLSessionTestIT extends BaseIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    private final ResponseGeneratorALLHelper responseGeneratorALLHelper = new ResponseGeneratorALLHelper();

    //-------------------------------------------------Non-Dynamic, not batched-----------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_complex_scheme_non_dynamic_not_batched.sql", "/scripts/_functional/case_complex_all.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void complexNonDynamicCaseS1T5Q50PerBatch1Incorrect1Test() {
        /**
         * UserId = 2L;
         * Complex case: complex scheme of 5 themes, each theme contains 10 questions, 5 out of 10 of each type are requested;
         * Single question per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: all are correct, but one incorrect (last one)
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            // here you show questions of the next batch
            QuestionSessionOutDto question = currentBatch.getQuestions().get(0);
            Long questionId = question.getQuestionId();
            long type = question.getType();
            Map<Long, Response> response = responseGeneratorALLHelper.getResponseMap(type, questionsMap, questionId, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        QuestionSessionOutDto question = currentBatch.getQuestions().get(0);
        Long questionId = question.getQuestionId();
        long type = question.getType();
        Map<Long, Response> response = responseGeneratorALLHelper.getResponseMap(type, questionsMap, questionId, false);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("96", resultOutDto.getPercent());
        assertEquals("5", resultOutDto.getGrade());
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
        assertEquals(96.0, result.getPercent(), 0.1);
        assertEquals(5.0, result.getGrade(), 0.1);

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
        assertEquals(100.0, resultTheme1.getPercent(), 0.1);

        ResultTheme resultTheme2 = resultThemes.get(1);
        assertEquals(2L, resultTheme2.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme2.getQuantity());
        assertEquals(100.0, resultTheme2.getPercent(), 0.1);

        ResultTheme resultTheme3 = resultThemes.get(2);
        assertEquals(3L, resultTheme3.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme3.getQuantity());
        assertEquals(100.0, resultTheme3.getPercent(), 0.1);

        ResultTheme resultTheme4 = resultThemes.get(3);
        assertEquals(4L, resultTheme4.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme4.getQuantity());
        assertEquals(100.0, resultTheme4.getPercent(), 0.1);

        ResultTheme resultTheme5 = resultThemes.get(4);
        assertEquals(5L, resultTheme5.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme5.getQuantity());
        assertEquals(80.0, resultTheme5.getPercent(), 0.1);
    }

    //-------------------------------------------------Non-Dynamic, batched 2-------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_complex_scheme_non_dynamic_batched_2.sql", "/scripts/_functional/case_complex_all.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void complexNonDynamicCaseS1T5Q50PerBatch2Correct13Test() {
        /*
         * UserId = 2L;
         * Complex case: simple scheme of single theme of 10 SQ questions, all are requested;
         * 2 questions per batch, non-dynamic implementation (you do know when to launch finish with last batch);
         * Non-LMS environment;
         * Scenario: Single incorrect per each batch, except the last batch where all are correct!
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        boolean isLastBatch = currentBatch.isLastBatch();
        while (!isLastBatch) {
            // here you show questions of the next batch
            Map<Long, Response> response = responseGeneratorALLHelper.getResponseMap(questionsMap, currentBatch, true);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
            isLastBatch = currentBatch.isLastBatch();
        }
        // Here you show the last question and collect the last response
        Map<Long, Response> response = responseGeneratorALLHelper.getResponseMap(questionsMap, currentBatch, false);
        ResultOutDto resultOutDto = genericSessionService.finish(new BatchInDto(response), sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("52", resultOutDto.getPercent());
        assertEquals("3", resultOutDto.getGrade());
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
        assertEquals(52.0, result.getPercent(), 0.1);
        assertEquals(3.0, result.getGrade(), 0.1);

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
        assertTrue(resultTheme1.getPercent() >= 40);

        ResultTheme resultTheme2 = resultThemes.get(1);
        assertEquals(2L, resultTheme2.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme2.getQuantity());
        assertTrue(resultTheme2.getPercent() >= 40);

        ResultTheme resultTheme3 = resultThemes.get(2);
        assertEquals(3L, resultTheme3.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme3.getQuantity());
        assertTrue(resultTheme3.getPercent() >= 40);

        ResultTheme resultTheme4 = resultThemes.get(3);
        assertEquals(4L, resultTheme4.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme4.getQuantity());
        assertTrue(resultTheme4.getPercent() >= 40);

        ResultTheme resultTheme5 = resultThemes.get(4);
        assertEquals(5L, resultTheme5.getTheme().getThemeId().longValue());
        assertEquals(5, resultTheme5.getQuantity());
        assertTrue(resultTheme5.getPercent() >= 40);
    }

}

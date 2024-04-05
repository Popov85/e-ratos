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
public class DynamicSQSessionTestIT {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenericSessionService genericSessionService;

    @Autowired
    private EducationalSessionService educationalSessionService;

    private ResponseGeneratorSQHelper responseGeneratorSQHelper = new ResponseGeneratorSQHelper();

    //------------------------------------------------------not batched-------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_sq_scheme_dynamic_not_batched.sql", "/scripts/_functional/case_simple_sq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1SQ10PerBatch1Skip2Correct0Test() {
        /*
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 SQ questions, all are requested;
         * Single question per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Non-pyramidal! Incorrect-s are not returned back! Never!
         * Non-LMS environment;
         * Scenario: Skipped 2 questions {1L, 9L}, no correct answers were provided;
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int skipCounter = 0;
        int skipCounter2 = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            Long questionId = currentBatch.getQuestions().get(0).getQuestionId();
            if (skipCounter == 0 && questionId.equals(1L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter++;
            } else if (skipCounter2 == 0 && questionId.equals(9L)) {
                educationalSessionService.skip(questionId, sessionData);
                currentBatch = genericSessionService.next(new BatchInDto(new HashMap<>()), sessionData);
                skipCounter2++;
            } else {
                Map<Long, Response> response = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, questionId, false);
                currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
                // Client just clicked button Next>>
            }
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertFalse(resultOutDto.isPassed());
        assertEquals("0", resultOutDto.getPercent());
        assertEquals("2", resultOutDto.getGrade());
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


    //-----------------------------------------------------batched 3----------------------------------------------------

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/_functional/case_simple_sq_scheme_dynamic_batched_2.sql", "/scripts/_functional/case_simple_sq.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void dynamicCaseS1T1SQ10PerBatch2Skip1Incorrect0Test() {
        /*
         * UserId = 2L;
         * Simplest case: simple scheme of single theme of 10 SQ questions, all are requested;
         * 2 questions per batch, dynamic implementation (you do NOT know when to launch finish with last batch);
         * Pyramidal!
         * Non-LMS environment;
         * Scenario: 1 prohibited skip in the first batch, all are correct.
         */
        SessionData sessionData = genericSessionService.start(1L);
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        BatchOutDto currentBatch = sessionData.getCurrentBatch().get();
        int batchCounter = 0;
        while (!currentBatch.isEmpty()) {// even if no batches left launch next to get empty batch out
            if (batchCounter == 0) {
                // skipped
                Long skippedQuestionId = currentBatch.getQuestions().get(0).getQuestionId();
                // perform skip
                try {
                    educationalSessionService.skip(skippedQuestionId, sessionData);
                } catch (Exception e) {
                }
                batchCounter++;
            }
            Map<Long, Response> response = responseGeneratorSQHelper.getCorrectResponseSQMap(questionsMap, currentBatch, false);
            currentBatch = genericSessionService.next(new BatchInDto(response), sessionData);
        }
        // Here you show the last question and collect the last response
        ResultOutDto resultOutDto = genericSessionService.finish(sessionData);

        // Expected result:
        assertTrue(resultOutDto.isPassed());
        assertEquals("100", resultOutDto.getPercent());
        assertEquals("1", resultOutDto.getGrade());
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

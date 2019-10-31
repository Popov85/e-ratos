package ua.edu.ratos.service.session;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ua.edu.ratos._helper.QuestionGeneratorHelper;
import ua.edu.ratos.service.domain.*;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
public class SessionDataServiceTest extends QuestionGeneratorHelper {

    @Autowired
    private SessionDataService sessionDataService;

    @TestConfiguration
    static class SessionDataServiceTestContextConfiguration {

        @Bean
        public SessionDataService sessionDataService() {
            return new SessionDataService();
        }
    }

    private SessionData sessionData;

    private BatchOutDto batchOutDto;

    @Before
    // The test's sql script ExecutionPhase.BEFORE_TEST_METHOD is invoked before @Before
    public void init() {

        SchemeDomain schemeDomain = new SchemeDomain()
                .setSchemeId(1L).setName("Scheme test")
                .setSettingsDomain(new SettingsDomain()
                        .setSetId(1L)
                        .setName("Settings test")
                        .setQuestionsPerSheet((short)5)
                        .setSecondsPerQuestion(60)
                        .setStrictControlTimePerQuestion(false))
                .setStrategyDomain(new StrategyDomain().setStrId(1L))
                .setModeDomain(new ModeDomain().setModeId(1L))
                .setGradingDomain(new GradingDomain().setGradingId(1L))
                .setOptionsDomain(new OptionsDomain().setOptId(1L));

        List<QuestionDomain> sequence = Arrays.asList(
                createMCQ(1L, "QuestionDomain Multiple Choice #1"),
                createFBSQ(2L, "QuestionDomain Fill Blank Single #2"),
                createFBMQ(3L, "QuestionDomain Fill Blank Multiple #3", true),
                createMQ(4L, "Matcher QuestionDomain #4", true),
                createSQ(5L, "Sequence question #5"));

        SessionData sessionData = SessionData.createNoLMS(1L, schemeDomain, sequence);

        this.sessionData = sessionData;

        List<QuestionSessionOutDto> batchQuestions = sequence
                .stream()
                .map(q -> q.toDto())
                .collect(Collectors.toList());

        BatchOutDto batchOutDto = BatchOutDto.createRegular(batchQuestions, true);

        this.batchOutDto = batchOutDto;
    }


    @Test
    public void updateTest()  {
        int currentIndexBefore = sessionData.getCurrentIndex();
        Optional<BatchOutDto> currentBatchBefore = sessionData.getCurrentBatch();
        LocalDateTime currentBatchIssuedBefore = sessionData.getCurrentBatchIssued();
        LocalDateTime currentBatchTimeoutBefore = sessionData.getCurrentBatchTimeout();
        // Actual test begins
        sessionDataService.update(sessionData, batchOutDto);

        int currentIndexAfter = sessionData.getCurrentIndex();
        assertNotEquals(currentIndexBefore, currentIndexAfter);
        assertEquals(5, currentIndexAfter);

        assertFalse(currentBatchBefore.isPresent());
        Optional<BatchOutDto> currentBatch = sessionData.getCurrentBatch();
        assertTrue(currentBatch.isPresent());

        LocalDateTime currentBatchIssuedAfter = this.sessionData.getCurrentBatchIssued();
        assertNotEquals(currentBatchIssuedBefore, currentBatchIssuedAfter);
        assertNotNull(currentBatchIssuedAfter);

        // Batch not time-limited
        LocalDateTime currentBatchTimeoutAfter = this.sessionData.getCurrentBatchTimeout();
        assertEquals(currentBatchTimeoutBefore, LocalDateTime.MAX);
        assertEquals(currentBatchTimeoutAfter, LocalDateTime.MAX);
    }

}

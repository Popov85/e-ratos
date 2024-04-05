package ua.edu.ratos.service.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ratos._helper.QuestionGeneratorHelper;
import ua.edu.ratos.service.domain.ResponseEvaluated;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.*;
import ua.edu.ratos.service.domain.response.*;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatchEvaluatorServiceTest {

    @InjectMocks
    private BatchEvaluatorService batchEvaluatorService;

    @Mock
    private Timeout timeout;

    @Mock
    private SessionData sessionData;

    private BatchOutDto batchOutDto;

    private Map<Long, QuestionDomain> questionsMap;

    @BeforeEach
    public void init() {
        QuestionGeneratorHelper qGH = new QuestionGeneratorHelper();

        QuestionMCQDomain mcq = qGH.createMCQ(1L, "QuestionDomain Multiple Choice #1");
        QuestionFBSQDomain fbsq = qGH.createFBSQ(2L, "QuestionDomain Fill Blank Single #2");
        QuestionFBMQDomain fbmq = qGH.createFBMQ(3L, "QuestionDomain Fill Blank Multiple #3", true);
        QuestionMQDomain mq = qGH.createMQ(4L, "Matcher QuestionDomain #4", true);
        QuestionSQDomain sq = qGH.createSQ(5L, "Sequence question #5");

        List<QuestionDomain> sequence = Arrays.asList(mcq, fbsq, fbmq, mq, sq);

        questionsMap = new HashMap<>();
        questionsMap.put(1L, mcq);
        questionsMap.put(2L, fbsq);
        questionsMap.put(3L, fbmq);
        questionsMap.put(4L, mq);
        questionsMap.put(5L, sq);

        this.batchOutDto = BatchOutDto.createRegular(sequence.stream().map(QuestionDomain::toDto).collect(Collectors.toList()), true);
    }

    @Test
    public void doEvaluateAll5CorrectTest() {
        when(timeout.isTimeouted()).thenReturn(false);
        when(sessionData.getCurrentBatch()).thenReturn(Optional.ofNullable(this.batchOutDto));
        when(sessionData.getQuestionsMap()).thenReturn(this.questionsMap);

        // Prepare all correct responses for each question of different type
        Map<Long, Response> responses = new HashMap<>();

        Response responseMCQ = new ResponseMCQ(1L, Set.of(1L));
        responses.put(1L, responseMCQ);

        Response responseFBSQ = new ResponseFBSQ(2L, "PhraseDomain #4");
        responses.put(2L, responseFBSQ);

        ResponseFBMQ.Pair p1 = new ResponseFBMQ.Pair(1L, "PhraseDomain #4 for FBMQ answer #1");
        ResponseFBMQ.Pair p2 = new ResponseFBMQ.Pair(2L, "PhraseDomain #4 for FBMQ answer #2");
        ResponseFBMQ.Pair p3 = new ResponseFBMQ.Pair(3L, "PhraseDomain #4 for FBMQ answer #3");
        ResponseFBMQ.Pair p4 = new ResponseFBMQ.Pair(4L, "PhraseDomain #4 for FBMQ answer #4");

        Response responseFBMQ = new ResponseFBMQ(3L, Set.of(p1, p2, p3, p4));
        responses.put(3L, responseFBMQ);

        ResponseMQ.Triple t1 = new ResponseMQ.Triple(1L, 21L, 22L);
        ResponseMQ.Triple t2 = new ResponseMQ.Triple(2L, 23L, 24L);
        ResponseMQ.Triple t3 = new ResponseMQ.Triple(3L, 25L, 26L);
        ResponseMQ.Triple t4 = new ResponseMQ.Triple(4L, 27L, 28L);
        Response responseMQ = new ResponseMQ(4L, Set.of(t1, t2, t3, t4));
        responses.put(4L, responseMQ);

        Response responseSQ = new ResponseSQ(5L, List.of(1L, 2L, 3L, 4L, 5L));
        responses.put(5L, responseSQ);

        BatchInDto batchInDto = new BatchInDto(responses);

        // Actual test begins
        List<ResponseEvaluated> list = batchEvaluatorService.doEvaluate(batchInDto, sessionData);

        assertEquals(5, list.size(), "Array length is not equal to 5");
        assertEquals(100.0, list.get(0).getScore(), 0.1, "First score is not 100.0");
        assertEquals(100.0, list.get(1).getScore(), 0.1, "Second score is not 100.0");
        assertEquals(100.0, list.get(2).getScore(), 0.1, "Third score is not 100.0");
        assertEquals(100.0, list.get(3).getScore(), 0.1, "Fourth score is not 100.0");
        assertEquals(100.0, list.get(4).getScore(), 0.1, "Fifth score is not 100.0");
    }


    @Test
    public void doEvaluateAll5InCorrectTest() {
        when(timeout.isTimeouted()).thenReturn(false);
        when(sessionData.getCurrentBatch()).thenReturn(Optional.ofNullable(this.batchOutDto));
        when(sessionData.getQuestionsMap()).thenReturn(this.questionsMap);

        // Prepare all incorrect responses
        Map<Long, Response> responses = new HashMap<>();

        Response responseMCQ = new ResponseMCQ(1L, Set.of(4L));
        responses.put(1L, responseMCQ);

        Response responseFBSQ = new ResponseFBSQ(2L, "PhraseDomain #4 Incorrect");
        responses.put(2L, responseFBSQ);

        ResponseFBMQ.Pair p1 = new ResponseFBMQ.Pair(1L, "PhraseDomain #4 for FBMQ answer #1 Incorrect");
        ResponseFBMQ.Pair p2 = new ResponseFBMQ.Pair(2L, "PhraseDomain #4 for FBMQ answer #2 Incorrect");
        ResponseFBMQ.Pair p3 = new ResponseFBMQ.Pair(3L, "PhraseDomain #4 for FBMQ answer #3 Incorrect");
        ResponseFBMQ.Pair p4 = new ResponseFBMQ.Pair(4L, "PhraseDomain #4 for FBMQ answer #4 Incorrect");

        Response responseFBMQ = new ResponseFBMQ(3L, Set.of(p1, p2, p3, p4));
        responses.put(3L, responseFBMQ);

        ResponseMQ.Triple t1 = new ResponseMQ.Triple(1L, 22L, 21L);
        ResponseMQ.Triple t2 = new ResponseMQ.Triple(2L, 24L, 23L);
        ResponseMQ.Triple t3 = new ResponseMQ.Triple(3L, 26L, 25L);
        ResponseMQ.Triple t4 = new ResponseMQ.Triple(4L, 28L, 27L);
        Response responseMQ = new ResponseMQ(4L, Set.of(t1, t2, t3, t4));
        responses.put(4L, responseMQ);

        Response responseSQ = new ResponseSQ(5L, List.of(5L, 4L, 3L, 2L, 1L));
        responses.put(5L, responseSQ);

        BatchInDto batchInDto = new BatchInDto(responses);

        // Actual test begin
        List<ResponseEvaluated> list = batchEvaluatorService.doEvaluate(batchInDto, sessionData);
        assertEquals(5, list.size(), "Array length is not equal to 5");
        assertEquals(0.0, list.get(0).getScore(), 0.1, "First score is not 0.0");
        assertEquals(0.0, list.get(1).getScore(), 0.1, "Second score is not 0.0");
        assertEquals(0.0, list.get(2).getScore(), 0.1, "Third score is not 0.0");
        assertEquals(0.0, list.get(3).getScore(), 0.1, "Fourth score is not 0.0");
        assertEquals(0.0, list.get(4).getScore(), 0.1, "Fifth score is not 0.0");

    }

    @Test
    public void doEvaluateAll3OutOf5CorrectTest() {
        when(timeout.isTimeouted()).thenReturn(false);
        when(sessionData.getCurrentBatch()).thenReturn(Optional.ofNullable(this.batchOutDto));
        when(sessionData.getQuestionsMap()).thenReturn(this.questionsMap);

        // Prepare 3 out of 5 correct responses
        Map<Long, Response> responses = new HashMap<>();

        Response responseMCQ = new ResponseMCQ(1L, Set.of(1L));
        responses.put(1L, responseMCQ);

        // Incorrect
        Response responseFBSQ = new ResponseFBSQ(2L, "PhraseDomain #4 Incorrect");
        responses.put(2L, responseFBSQ);

        ResponseFBMQ.Pair p1 = new ResponseFBMQ.Pair(1L, "PhraseDomain #4 for FBMQ answer #1");
        ResponseFBMQ.Pair p2 = new ResponseFBMQ.Pair(2L, "PhraseDomain #4 for FBMQ answer #2");
        ResponseFBMQ.Pair p3 = new ResponseFBMQ.Pair(3L, "PhraseDomain #4 for FBMQ answer #3");
        ResponseFBMQ.Pair p4 = new ResponseFBMQ.Pair(4L, "PhraseDomain #4 for FBMQ answer #4");

        Response responseFBMQ = new ResponseFBMQ(3L, Set.of(p1, p2, p3, p4));
        responses.put(3L, responseFBMQ);

        ResponseMQ.Triple t1 = new ResponseMQ.Triple(1L, 21L, 22L);
        ResponseMQ.Triple t2 = new ResponseMQ.Triple(2L, 23L, 24L);
        ResponseMQ.Triple t3 = new ResponseMQ.Triple(3L, 25L, 26L);
        ResponseMQ.Triple t4 = new ResponseMQ.Triple(4L, 27L, 28L);
        Response responseMQ = new ResponseMQ(4L, Set.of(t1, t2, t3, t4));
        responses.put(4L, responseMQ);

        // Incorrect
        Response responseSQ = new ResponseSQ(5L, List.of(1L, 2L, 5L, 4L, 3L));
        responses.put(5L, responseSQ);

        BatchInDto batchInDto = new BatchInDto(responses);

        // Actual test begins
        List<ResponseEvaluated> list = batchEvaluatorService.doEvaluate(batchInDto, sessionData);
        assertEquals(5, list.size(), "Array length is not equal to 5");
        assertEquals(100.0, list.get(0).getScore(), 0.1, "First score is not 100.0");
        assertEquals(0.0, list.get(1).getScore(), 0.1, "Second score is not 0.0");
        assertEquals(100.0, list.get(2).getScore(), 0.1, "Third score is not 100.0");
        assertEquals(100.0, list.get(3).getScore(), 0.1, "Fourth score is not 100.0");
        assertEquals(0.0, list.get(4).getScore(), 0.1, "Fifth score is not 0.0");
    }


    @Test
    public void doEvaluatePartlyWherePossibleCorrectTest() {
        when(timeout.isTimeouted()).thenReturn(false);
        when(sessionData.getCurrentBatch()).thenReturn(Optional.ofNullable(this.batchOutDto));
        when(sessionData.getQuestionsMap()).thenReturn(this.questionsMap);

        // Prepare partly where possible correct responses
        Map<Long, Response> responses = new HashMap<>();

        Response responseMCQ = new ResponseMCQ(1L, Set.of(1L));
        responses.put(1L, responseMCQ);

        Response responseFBSQ = new ResponseFBSQ(2L, "PhraseDomain #4");
        responses.put(2L, responseFBSQ);

        // Partly
        ResponseFBMQ.Pair p1 = new ResponseFBMQ.Pair(1L, "PhraseDomain #4 for FBMQ answer #1");
        ResponseFBMQ.Pair p2 = new ResponseFBMQ.Pair(2L, "PhraseDomain #4 for FBMQ answer #2 Incorrect");
        ResponseFBMQ.Pair p3 = new ResponseFBMQ.Pair(3L, "PhraseDomain #4 for FBMQ answer #3");
        ResponseFBMQ.Pair p4 = new ResponseFBMQ.Pair(4L, "PhraseDomain #4 for FBMQ answer #4 Incorrect");

        Response responseFBMQ = new ResponseFBMQ(3L, Set.of(p1, p2, p3, p4));
        responses.put(3L, responseFBMQ);

        // Partly
        ResponseMQ.Triple t1 = new ResponseMQ.Triple(1L, 21L, 22L);
        ResponseMQ.Triple t2 = new ResponseMQ.Triple(2L, 24L, 23L);
        ResponseMQ.Triple t3 = new ResponseMQ.Triple(3L, 25L, 26L);
        ResponseMQ.Triple t4 = new ResponseMQ.Triple(4L, 28L, 27L);
        Response responseMQ = new ResponseMQ(4L, Set.of(t1, t2, t3, t4));
        responses.put(4L, responseMQ);

        Response responseSQ = new ResponseSQ(5L, List.of(1L, 2L, 3L, 4L, 5L));
        responses.put(5L, responseSQ);

        BatchInDto batchInDto = new BatchInDto(responses);

        // Actual test begins
        List<ResponseEvaluated> list = batchEvaluatorService.doEvaluate(batchInDto, sessionData);
        assertEquals(5, list.size(), "Array length is not equal to 5");
        assertEquals(100.0, list.get(0).getScore(), 0.1, "First score is not 100.0");
        assertEquals(100.0, list.get(1).getScore(), 0.1, "Second score is not 100.0");
        assertEquals(50.0, list.get(2).getScore(), 0.1, "Third score is not 50.0");
        assertEquals(50.0, list.get(3).getScore(), 0.1, "Fourth score is not 50.0");
        assertEquals(100.0, list.get(4).getScore(), 0.1, "Fifth score is not 100.0");
    }

    @Test
    public void doEvaluateEmptyTest() {
        when(sessionData.getCurrentBatch()).thenReturn(Optional.ofNullable(this.batchOutDto));
        when(sessionData.getQuestionsMap()).thenReturn(this.questionsMap);
        // Prepare empty batch of responses
        Map<Long, Response> responses = new HashMap<>();

        BatchInDto batchInDto = new BatchInDto(responses);

        // Actual test begins
        List<ResponseEvaluated> list = batchEvaluatorService.doEvaluate(batchInDto, sessionData);
        assertEquals(5, list.size(), "Array length is not equal to 5");
        assertEquals(0.0, list.get(0).getScore(), 0.1, "First score is not 0.0");
        assertEquals(0.0, list.get(1).getScore(), 0.1, "Second score is not 0.0");
        assertEquals(0.0, list.get(2).getScore(), 0.1, "Third score is not 0.0");
        assertEquals(0.0, list.get(3).getScore(), 0.1, "Fourth score is not 0.0");
        assertEquals(0.0, list.get(4).getScore(), 0.1, "Fifth score is not 0.0");
    }


    @Test
    @SuppressWarnings("DataFlowIssue")
    public void doEvaluateNullableInputTest() {
        // Actual test begins
        assertThrows(NullPointerException.class, () -> {
            batchEvaluatorService.doEvaluate(null, sessionData);
        });
    }

    @Test
    public void doEvaluateNullableCurrentBatchTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            sessionData.setCurrentBatch(null);
            Map<Long, Response> responses = new HashMap<>();
            BatchInDto batchInDto = new BatchInDto(responses);
            // Actual test begins
            batchEvaluatorService.doEvaluate(batchInDto, sessionData);
        });
    }

}

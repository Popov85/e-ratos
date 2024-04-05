package ua.edu.ratos.service.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.question.QuestionMCQDomain;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShiftServiceTest {

    @Mock
    private SessionData sessionData;

    private List<QuestionDomain> questionDomains;

    private Map<Long, QuestionDomain> questionsMap;

    @BeforeEach
    public void setUp() {
        QuestionDomain q1 = createQuestion(1L, "Q1");
        QuestionDomain q2 = createQuestion(2L, "Q2");
        QuestionDomain q3 = createQuestion(3L, "Q3");
        QuestionDomain q4 = createQuestion(4L, "Q4");
        QuestionDomain q5 = createQuestion(5L, "Q5");

        questionDomains = new ArrayList<>(List.of(q1, q2, q3, q4, q5));

        questionsMap = new HashMap<>();
        questionsMap.put(1L, q1);
        questionsMap.put(2L, q2);
        questionsMap.put(3L, q3);
        questionsMap.put(4L, q4);
        questionsMap.put(5L, q5);
    }

    @Test
    public void doShiftOneFromTheBeginningTest() {
        // Prepare mocks
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting 1L
        Long idToShift = 1L;

        // Actual test begins
        shiftService.doShift(idToShift, sessionData);

        // Verify mocks invocation
        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(2L, 3L, 4L, 5L, 1L), is(equalTo(actual)));
    }

    @Test
    public void doShiftOneFromTheMiddleTest() {
        // Prepare mocks
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting 3L
        Long idToShift = 3L;

        // Actual test begins
        shiftService.doShift(idToShift, sessionData);

        // Verify mocks invocation
        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(1L, 2L, 4L, 5L, 3L), is(equalTo(actual)));
    }

    @Test
    public void doShiftOneFromTheEndTest() {
        // Prepare mocks
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting 5L
        Long idToShift = 5L;

        // Actual test begins
        shiftService.doShift(idToShift, sessionData);

        // Verify mocks invocation
        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(1L, 2L, 3L, 4L, 5L), is(equalTo(actual)));
    }

    @Test
    public void doShiftTwoTest() {
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting 2L and 4L
        List<Long> idsToShift = new ArrayList<>(Arrays.asList(2L, 4L));

        shiftService.doShift(idsToShift, sessionData);

        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(1L, 3L, 5L, 2L, 4L), is(equalTo(actual)));
    }

    @Test
    public void doShiftAllTest() {
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting all
        List<Long> idsToShift = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L));

        shiftService.doShift(idsToShift, sessionData);

        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(1L, 2L, 3L, 4L, 5L), is(equalTo(actual)));
    }

    @Test
    public void doShiftOneNotPresentShouldBeIgnoredTest() {
        // Prepare mocks
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting 1000L - not such questionId in sessionData
        Long idToShift = 1000L;

        // Actual test begins
        shiftService.doShift(idToShift, sessionData);

        // Verify mocks invocation
        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(1L, 2L, 3L, 4L, 5L), is(equalTo(actual)));
    }

    @Test
    public void doShiftManyNotPresentShouldBeIgnoredTest() {
        // Prepare mocks
        when(sessionData.getSequence()).thenReturn(questionDomains);
        when(sessionData.getQuestionsMap()).thenReturn(questionsMap);

        ShiftService shiftService = new ShiftService();

        // Shifting 1000L, 2000L, 3000L - not such questionId-s in sessionData
        List<Long> idsToShift = new ArrayList<>(Arrays.asList(1000L, 2000L, 3000L));

        // Actual test begins
        shiftService.doShift(idsToShift, sessionData);

        // Verify mocks invocation
        verify(sessionData, times(1)).getSequence();
        verify(sessionData, times(1)).getQuestionsMap();

        // Prepare actual list of IDs of compare
        List<Long> actual = questionDomains.stream().map(QuestionDomain::getQuestionId).collect(Collectors.toList());

        assertThat("After shifting, the resulting array is broken", List.of(1L, 2L, 3L, 4L, 5L), is(equalTo(actual)));
    }

    private QuestionDomain createQuestion(Long id, String title) {
        QuestionDomain q = new QuestionMCQDomain();
        q.setQuestionId(id);
        q.setQuestion(title);
        return q;
    }

}

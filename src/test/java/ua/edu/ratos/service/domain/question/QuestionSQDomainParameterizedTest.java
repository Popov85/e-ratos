package ua.edu.ratos.service.domain.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.service.domain.PhraseDomain;
import ua.edu.ratos.service.domain.answer.AnswerSQDomain;
import ua.edu.ratos.service.domain.response.ResponseSQ;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuestionSQDomainParameterizedTest {

    private QuestionSQDomain question;

    @BeforeEach
    public void init() {
        question = new QuestionSQDomain();
        question.setQuestionId(1L);
        Set<AnswerSQDomain> answers = new HashSet<>();
        answers.add(createAnswer(1000L, (short) 0));
        answers.add(createAnswer(1001L, (short) 1));
        answers.add(createAnswer(1002L, (short) 2));
        answers.add(createAnswer(1003L, (short) 3));
        answers.add(createAnswer(1004L, (short) 4));
        question.setAnswers(answers);
    }

    @ParameterizedTest(name = "{index}: evaluateTest({0}) = {1} exception {2}")
    @MethodSource("data")
    public void evaluateTest(ResponseSQ response, int expected, Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            assertThrows(expectedException, () -> question.evaluate(response));
        } else {
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }
    }

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(null, 0, NullPointerException.class),
                Arguments.of(new ResponseSQ(1L, null), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of()), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1001L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1002L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1003L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1004L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1001L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1002L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1003L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1004L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1001L, 1002L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1001L, 1003L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1001L, 1004L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1004L, 1003L, 1002L, 1001L, 1000L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(2000L, 2001L, 2002L, 2003L, 2004L)), 0, null),
                Arguments.of(new ResponseSQ(1L, List.of(1000L, 1001L, 1002L, 1003L, 1004L)), 100, null)
        );
    }

    private AnswerSQDomain createAnswer(Long answerId, short order) {
        AnswerSQDomain answer = new AnswerSQDomain();
        answer.setAnswerId(answerId);
        PhraseDomain phraseDomain = new PhraseDomain();
        answer.setPhraseDomain(phraseDomain);
        answer.setOrder(order);
        return answer;
    }
}
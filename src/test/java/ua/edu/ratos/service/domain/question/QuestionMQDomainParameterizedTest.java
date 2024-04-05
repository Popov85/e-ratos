package ua.edu.ratos.service.domain.question;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.service.domain.PhraseDomain;
import ua.edu.ratos.service.domain.answer.AnswerMQDomain;
import ua.edu.ratos.service.domain.response.ResponseMQ;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionMQDomainParameterizedTest {
    private static final String[] SAMPLES0 = new String[]{"phrase0", "zero phraseDomain"};
    private static final String[] SAMPLES1 = new String[]{"phrase1", "first phraseDomain"};
    private static final String[] SAMPLES2 = new String[]{"phrase2", "second phraseDomain"};
    private static final String[] SAMPLES3 = new String[]{"phrase3", "third phraseDomain"};
    private static final String[] SAMPLES4 = new String[]{"phrase4", "fourth phraseDomain"};

    @Nested
    class PartialResponseAllowedTest {

        private QuestionMQDomain question;

        @BeforeEach
        public void init() {
            question = new QuestionMQDomain();
            question.setQuestionId(1L);
            AnswerMQDomain answer0 = createAnswer(1000l, 2000l, 2001l, SAMPLES0);
            AnswerMQDomain answer1 = createAnswer(1001l, 2002l, 2003l, SAMPLES1);
            AnswerMQDomain answer2 = createAnswer(1002l, 2004l, 2005l, SAMPLES2);
            AnswerMQDomain answer3 = createAnswer(1003l, 2006l, 2007l, SAMPLES3);
            AnswerMQDomain answer4 = createAnswer(1004l, 2008l, 2009l, SAMPLES4);
            question.setAnswers(new HashSet<>(Arrays.asList(answer0, answer1, answer2, answer3, answer4)));
            question.setPartialResponseAllowed(false);
        }

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMQ response, double expected, Class<? extends Exception> expectedException) {
            if (expectedException != null) {
                Exception exception = assertThrows(expectedException, () -> question.evaluate(response));
                assertTrue(expectedException.isAssignableFrom(exception.getClass()));
            } else {
                assertEquals(expected, question.evaluate(response), 0.01);
            }
        }

        static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(null, 0, NullPointerException.class),
                    Arguments.of(new ResponseMQ(1L, null), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>()), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2003L),
                            new ResponseMQ.Triple(1001L, 2002L, 2003L),
                            new ResponseMQ.Triple(1002L, 2004L, 2005L),
                            new ResponseMQ.Triple(1003L, 2006L, 2007L),
                            new ResponseMQ.Triple(1004L, 2008L, 2009L)))), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2001L),
                            new ResponseMQ.Triple(1001L, 2002L, 2001L),
                            new ResponseMQ.Triple(1002L, 2004L, 2009L),
                            new ResponseMQ.Triple(1003L, 2006L, 2005L),
                            new ResponseMQ.Triple(1004L, 2008L, 2007L)))), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2001L, 2000L),
                            new ResponseMQ.Triple(1001L, 2003L, 2002L),
                            new ResponseMQ.Triple(1002L, 2005L, 2004L),
                            new ResponseMQ.Triple(1003L, 2007L, 2006L),
                            new ResponseMQ.Triple(1004L, 2009L, 2008L)))), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2001L, 3000L),
                            new ResponseMQ.Triple(1001L, 2003L, 3002L),
                            new ResponseMQ.Triple(1002L, 2005L, 3004L),
                            new ResponseMQ.Triple(1003L, 2007L, 3006L),
                            new ResponseMQ.Triple(1004L, 2009L, 3008L)))), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 3001L, 2000L),
                            new ResponseMQ.Triple(1001L, 3003L, 2002L),
                            new ResponseMQ.Triple(1002L, 3005L, 2004L),
                            new ResponseMQ.Triple(1003L, 3007L, 2006L),
                            new ResponseMQ.Triple(1004L, 3009L, 2008L)))), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2001L),
                            new ResponseMQ.Triple(1001L, 2002L, 2003L),
                            new ResponseMQ.Triple(1002L, 2004L, 2005L),
                            new ResponseMQ.Triple(1003L, 2006L, 2007L),
                            new ResponseMQ.Triple(1004L, 2008L, 2009L)))), 100, null)
            );
        }
    }

    @Nested
    public class PartialResponseNotAllowedTest {

        private QuestionMQDomain question;

        @BeforeEach
        public void init() {
            question = new QuestionMQDomain();
            question.setQuestionId(1L);
            AnswerMQDomain answer0 = createAnswer(1000L, 2000L, 2001L, SAMPLES0);
            AnswerMQDomain answer1 = createAnswer(1001L, 2002L, 2003L, SAMPLES1);
            AnswerMQDomain answer2 = createAnswer(1002L, 2004L, 2005L, SAMPLES2);
            AnswerMQDomain answer3 = createAnswer(1003L, 2006L, 2007L, SAMPLES3);
            AnswerMQDomain answer4 = createAnswer(1004L, 2008L, 2009L, SAMPLES4);
            question.setAnswers(new HashSet<>(Arrays.asList(answer0, answer1, answer2, answer3, answer4)));
            question.setPartialResponseAllowed(true);
        }

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMQ response, double expected, Class<? extends Exception> expectedException) {
            if (expectedException != null) {
                assertThrows(expectedException, () -> question.evaluate(response));
            } else {
                assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
            }
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(null, 0, NullPointerException.class),
                    Arguments.of(new ResponseMQ(1L, null), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>()), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2001L, 2000L),
                            new ResponseMQ.Triple(1001L, 2002L, 2001L),
                            new ResponseMQ.Triple(1002L, 2004L, 2009L),
                            new ResponseMQ.Triple(1003L, 2006L, 2005L),
                            new ResponseMQ.Triple(1004L, 2008L, 2007L)))), 0, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2001L),
                            new ResponseMQ.Triple(1001L, 2002L, 2001L),
                            new ResponseMQ.Triple(1002L, 2004L, 2009L),
                            new ResponseMQ.Triple(1003L, 2006L, 2005L),
                            new ResponseMQ.Triple(1004L, 2008L, 2007L)))), 20, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2003L),
                            new ResponseMQ.Triple(1001L, 2002L, 2003L),
                            new ResponseMQ.Triple(1002L, 2004L, 2005L),
                            new ResponseMQ.Triple(1003L, 2006L, 2009L),
                            new ResponseMQ.Triple(1004L, 2008L, 2007L)))), 40, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2003L),
                            new ResponseMQ.Triple(1001L, 2002L, 2003L),
                            new ResponseMQ.Triple(1002L, 2004L, 2005L),
                            new ResponseMQ.Triple(1003L, 2006L, 2007L),
                            new ResponseMQ.Triple(1004L, 2009L, 2008L)))), 60, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2001L),
                            new ResponseMQ.Triple(1001L, 2002L, 2003L),
                            new ResponseMQ.Triple(1002L, 2004L, 2005L),
                            new ResponseMQ.Triple(1003L, 2006L, 2007L),
                            new ResponseMQ.Triple(1004L, 2009L, 2008L)))), 80, null),
                    Arguments.of(new ResponseMQ(1L, new HashSet<>(Arrays.asList(
                            new ResponseMQ.Triple(1000L, 2000L, 2001L),
                            new ResponseMQ.Triple(1001L, 2002L, 2003L),
                            new ResponseMQ.Triple(1002L, 2004L, 2005L),
                            new ResponseMQ.Triple(1003L, 2006L, 2007L),
                            new ResponseMQ.Triple(1004L, 2008L, 2009L)))), 100, null)
            );
        }
    }

    private static AnswerMQDomain createAnswer(Long answerId, Long leftPhraseId, Long rightPhraseId, String[] samples) {
        AnswerMQDomain answer = new AnswerMQDomain();
        answer.setAnswerId(answerId);
        PhraseDomain phraseDomain0Left = new PhraseDomain();
        phraseDomain0Left.setPhraseId(leftPhraseId);
        phraseDomain0Left.setPhrase(samples[0]);
        answer.setLeftPhraseDomain(phraseDomain0Left);
        PhraseDomain phraseDomain0Right = new PhraseDomain();
        phraseDomain0Right.setPhraseId(rightPhraseId);
        phraseDomain0Right.setPhrase(samples[1]);
        answer.setRightPhraseDomain(phraseDomain0Right);
        return answer;
    }
}

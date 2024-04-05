package ua.edu.ratos.service.domain.question;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.service.domain.answer.AnswerMCQDomain;
import ua.edu.ratos.service.domain.response.ResponseMCQ;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionMCQDomainParameterizedTest {

    @Nested
    public class OneCorrectRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 0, false));
            answers.add(createAnswer(1001L, "Answer1", (short) 0, false));
            answers.add(createAnswer(1002L, "Answer2", (short) 100, true));
            answers.add(createAnswer(1003L, "Answer3", (short) 0, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        private static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 100)
            );
        }
    }

    @Nested
    public class OneCorrectNotRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 0, false));
            answers.add(createAnswer(1001L, "Answer1", (short) 0, false));
            answers.add(createAnswer(1002L, "Answer2", (short) 100, false));
            answers.add(createAnswer(1003L, "Answer3", (short) 0, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 100)
            );
        }
    }

    @Nested
    public class TwoCorrectRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 50, true));
            answers.add(createAnswer(1001L, "Answer1", (short) 0, false));
            answers.add(createAnswer(1002L, "Answer2", (short) 50, true));
            answers.add(createAnswer(1003L, "Answer3", (short) 0, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L)), 100),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L, 0L)), 100)
            );
        }
    }

    @Nested
    public class TwoCorrectNotRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 50, false));
            answers.add(createAnswer(1001L, "Answer1", (short) 0, false));
            answers.add(createAnswer(1002L, "Answer2", (short) 50, false));
            answers.add(createAnswer(1003L, "Answer3", (short) 0, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L)), 100)
            );
        }
    }

    @Nested
    public class TwoCorrectOneRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 50, true));
            answers.add(createAnswer(1001L, "Answer1", (short) 0, false));
            answers.add(createAnswer(1002L, "Answer2", (short) 50, false));
            answers.add(createAnswer(1003L, "Answer3", (short) 0, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<org.junit.jupiter.params.provider.Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L)), 100)
            );
        }
    }

    @Nested
    public class AllCorrectRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 25, true));
            answers.add(createAnswer(1001L, "Answer1", (short) 25, true));
            answers.add(createAnswer(1002L, "Answer2", (short) 25, true));
            answers.add(createAnswer(1003L, "Answer3", (short) 25, true));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 100)
            );
        }
    }

    @Nested
    public class AllCorrectNotRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Answer0", (short) 25, false));
            answers.add(createAnswer(1001L, "Answer1", (short) 25, false));
            answers.add(createAnswer(1002L, "Answer2", (short) 25, false));
            answers.add(createAnswer(1003L, "Answer3", (short) 25, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 25),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L)), 25),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 25),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1003L)), 25),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1003L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1003L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L, 1003L)), 50),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L)), 75),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1003L)), 75),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L, 1003L)), 75),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L, 1003L)), 75),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 100)
            );
        }
    }


    @Nested
    public class MultipleCorrectMultipleRequiredTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseMCQ response, int expected) {
            QuestionMCQDomain question = new QuestionMCQDomain();
            question.setQuestionId(1L);
            Set<AnswerMCQDomain> answers = new HashSet<>();
            answers.add(createAnswer(1000L, "Title0", (short) 0, false));
            answers.add(createAnswer(1001L, "Title1", (short) 33, true));
            answers.add(createAnswer(1002L, "Title2", (short) 33, true));
            answers.add(createAnswer(1003L, "Title3", (short) 0, false));
            answers.add(createAnswer(1004L, "Title4", (short) 34, false));
            question.setAnswers(answers);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseMCQ(1L, null), 0),
                    Arguments.of(new ResponseMCQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1002L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1003L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1002L, 1003L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L, 1003L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1000L, 1001L, 1002L, 1003L, 1004L)), 0),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L)), 66),
                    Arguments.of(new ResponseMCQ(1L, Set.of(1001L, 1002L, 1004L)), 100)
            );
        }
    }

    private static AnswerMCQDomain createAnswer(Long id, String title, short percentage, boolean isRequired) {
        AnswerMCQDomain answer = new AnswerMCQDomain();
        answer.setAnswerId(id);
        answer.setAnswer(title);
        answer.setPercent(percentage);
        answer.setRequired(isRequired);
        return answer;
    }
}

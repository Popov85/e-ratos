package ua.edu.ratos.service.domain.question;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.service.domain.PhraseDomain;
import ua.edu.ratos.service.domain.SettingsFBDomain;
import ua.edu.ratos.service.domain.answer.AnswerFBMQDomain;
import ua.edu.ratos.service.domain.response.ResponseFBMQ;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionFBMQDomainParameterizedTest {

    private static final String PHRASE0 = "first phrase";
    private static final String[] ACCEPTED_PHRASES0 = new String[]{"phrase1", "phrase#1", "phrase one"};
    private static final String PHRASE1 = "second phrase";
    private static final String[] ACCEPTED_PHRASES1 = new String[]{"phrase2", "phrase#2", "phrase two"};
    private static final String PHRASE2 = "third phrase";
    private static final String[] ACCEPTED_PHRASES2 = new String[]{"phrase3", "phrase#3", "phrase three"};

    @Nested
    public class CaseSensitiveNoTyposAllowedPartialResponseNotAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseFBMQ response, int expected) {
            QuestionFBMQDomain question = new QuestionFBMQDomain();
            question.setQuestionId(1L);

            question.setAnswers(new HashSet<>(Arrays.asList(
                    createAnswer(1000L, PHRASE0, ACCEPTED_PHRASES0),
                    createAnswer(1001L, PHRASE1, ACCEPTED_PHRASES1),
                    createAnswer(1002L, PHRASE2, ACCEPTED_PHRASES2))));

            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(true);
            settings.setTypoAllowed(false);

            question.getAnswers().forEach(a -> a.setSettings(settings));
            question.setPartialResponseAllowed(false);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBMQ(1L, null), 0),
                    Arguments.of(new ResponseFBMQ(1L, new HashSet<>()), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase"),
                            new ResponseFBMQ.Pair(1001L, "phrase22"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase22"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "Phrase3")
                    )), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "prase3")
                    )), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "phrase4")
                    )), 0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "phrase#3")
                    )), 100)
            );
        }
    }

    @Nested
    class CaseSensitiveNoTyposAllowedPartialResponseAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseFBMQ response, double expected) {
            QuestionFBMQDomain question = new QuestionFBMQDomain();
            question.setQuestionId(1L);

            question.setAnswers(new HashSet<>(Arrays.asList(
                    createAnswer(1000L, PHRASE0, ACCEPTED_PHRASES0),
                    createAnswer(1001L, PHRASE1, ACCEPTED_PHRASES1),
                    createAnswer(1002L, PHRASE2, ACCEPTED_PHRASES2))));

            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(true);
            settings.setTypoAllowed(false);

            question.getAnswers().forEach(a -> a.setSettings(settings));
            question.setPartialResponseAllowed(true);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBMQ(1L, null), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, new HashSet<>()), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase"),
                            new ResponseFBMQ.Pair(1001L, "phrase22"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "1phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase wo"),
                            new ResponseFBMQ.Pair(1002L, "Phrase3")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase100"),
                            new ResponseFBMQ.Pair(1001L, "phrase tw1"),
                            new ResponseFBMQ.Pair(1002L, "prase3")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase"),
                            new ResponseFBMQ.Pair(1001L, "phrase"),
                            new ResponseFBMQ.Pair(1002L, "phrase4")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase22"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 33.33),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 66.66),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "phrase#3")
                    )), 100.0)
            );
        }
    }

    @Nested
    class CaseSensitiveTyposAllowedPartialResponseAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        void evaluateTest(ResponseFBMQ response, double expected) {
            QuestionFBMQDomain question = new QuestionFBMQDomain();
            question.setQuestionId(1L);

            question.setAnswers(new HashSet<>(Arrays.asList(
                    createAnswer(1000L, PHRASE0, ACCEPTED_PHRASES0),
                    createAnswer(1001L, PHRASE1, ACCEPTED_PHRASES1),
                    createAnswer(1002L, PHRASE2, ACCEPTED_PHRASES2))));

            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(true);
            settings.setTypoAllowed(true);

            question.getAnswers().forEach(a -> a.setSettings(settings));
            question.setPartialResponseAllowed(true);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBMQ(1L, null), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, new HashSet<>()), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "PHRASE ONE"),
                            new ResponseFBMQ.Pair(1001L, "Phrase22"),
                            new ResponseFBMQ.Pair(1002L, "33Phrase")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "1phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase wow"),
                            new ResponseFBMQ.Pair(1002L, "PHrase3")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "Phrase100"),
                            new ResponseFBMQ.Pair(1001L, "Phrase tw1"),
                            new ResponseFBMQ.Pair(1002L, "Prase3")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "PHRASE"),
                            new ResponseFBMQ.Pair(1001L, "Phrase"),
                            new ResponseFBMQ.Pair(1002L, "pHrase4")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "prase1"),
                            new ResponseFBMQ.Pair(1001L, "Phrase 22"),
                            new ResponseFBMQ.Pair(1002L, "33 phrase")
                    )), 33.33),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase!"),
                            new ResponseFBMQ.Pair(1001L, "phras two"),
                            new ResponseFBMQ.Pair(1002L, "33 phrase")
                    )), 66.66),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phra$e1"),
                            new ResponseFBMQ.Pair(1001L, "phrase tw0"),
                            new ResponseFBMQ.Pair(1002L, "phrase_3")
                    )), 100.0)
            );
        }
    }

    @Nested
    class NonCaseSensitiveTyposAllowedPartialResponseAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        void evaluateTest(ResponseFBMQ response, double expected) {
            QuestionFBMQDomain question = new QuestionFBMQDomain();
            question.setQuestionId(1L);

            question.setAnswers(new HashSet<>(Arrays.asList(
                    createAnswer(1000L, PHRASE0, ACCEPTED_PHRASES0),
                    createAnswer(1001L, PHRASE1, ACCEPTED_PHRASES1),
                    createAnswer(1002L, PHRASE2, ACCEPTED_PHRASES2))));

            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(false);
            settings.setTypoAllowed(true);

            question.getAnswers().forEach(a -> a.setSettings(settings));
            question.setPartialResponseAllowed(true);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBMQ(1L, null), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, new HashSet<>()), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase zero"),
                            new ResponseFBMQ.Pair(1001L, "phrase 22"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "1phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase 22"),
                            new ResponseFBMQ.Pair(1002L, "Phrase 33")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase100"),
                            new ResponseFBMQ.Pair(1001L, "phrase tw11"),
                            new ResponseFBMQ.Pair(1002L, "prase33")
                    )), 0.0),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "1-st phrase"),
                            new ResponseFBMQ.Pair(1001L, "phrase 1 ONE"),
                            new ResponseFBMQ.Pair(1002L, "PHRASE4")
                    )), 33.33),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase222"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 33.33),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "phrase two"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 66.66),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "PRASE1"),
                            new ResponseFBMQ.Pair(1001L, "phrase TWO"),
                            new ResponseFBMQ.Pair(1002L, "33phrase")
                    )), 66.66),
                    Arguments.of(new ResponseFBMQ(1L, Set.of(
                            new ResponseFBMQ.Pair(1000L, "phrase1"),
                            new ResponseFBMQ.Pair(1001L, "PHRASE two"),
                            new ResponseFBMQ.Pair(1002L, "pHrase#3")
                    )), 100.0)
            );
        }
    }

    private static AnswerFBMQDomain createAnswer(final Long answerId, final String phrase, final String[] acceptedPhrases) {
        AnswerFBMQDomain answer = new AnswerFBMQDomain();
        answer.setAnswerId(answerId);
        answer.setPhrase(phrase);
        PhraseDomain phraseDomain0 = new PhraseDomain();
        phraseDomain0.setPhrase(acceptedPhrases[0]);
        PhraseDomain phraseDomain1 = new PhraseDomain();
        phraseDomain1.setPhrase(acceptedPhrases[1]);
        PhraseDomain phraseDomain2 = new PhraseDomain();
        phraseDomain2.setPhrase(acceptedPhrases[2]);
        answer.setAcceptedPhraseDomains(new HashSet<>(Arrays.asList(phraseDomain0, phraseDomain1, phraseDomain2)));
        return answer;
    }
}

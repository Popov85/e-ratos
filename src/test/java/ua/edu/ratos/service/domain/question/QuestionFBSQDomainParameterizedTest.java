package ua.edu.ratos.service.domain.question;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.edu.ratos.service.domain.PhraseDomain;
import ua.edu.ratos.service.domain.SettingsFBDomain;
import ua.edu.ratos.service.domain.answer.AnswerFBSQDomain;
import ua.edu.ratos.service.domain.response.ResponseFBSQ;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionFBSQDomainParameterizedTest {

    private static final String[] ACCEPTED_PHRASES = new String[]{"phrase1", "phrase#1", "phrase one"};

    @Nested
    public class CaseSensitiveNoTyposAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseFBSQ response, int expected) {
            QuestionFBSQDomain question = new QuestionFBSQDomain();
            question.setQuestionId(1L);
            AnswerFBSQDomain answer = new AnswerFBSQDomain();
            PhraseDomain phraseDomain0 = new PhraseDomain();
            phraseDomain0.setPhrase(ACCEPTED_PHRASES[0]);
            PhraseDomain phraseDomain1 = new PhraseDomain();
            phraseDomain1.setPhrase(ACCEPTED_PHRASES[1]);
            PhraseDomain phraseDomain2 = new PhraseDomain();
            phraseDomain2.setPhrase(ACCEPTED_PHRASES[2]);
            PhraseDomain[] phraseDomains = {phraseDomain0, phraseDomain1, phraseDomain2};
            answer.setAcceptedPhraseDomains(new HashSet<>(Arrays.asList(phraseDomains)));
            question.setAnswer(answer);
            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(true);
            settings.setTypoAllowed(false);
            answer.setSettings(settings);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBSQ(1L, null), 0),
                    Arguments.of(new ResponseFBSQ(1L, ""), 0),
                    Arguments.of(new ResponseFBSQ(1L, "000"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "string1"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "phrase 1"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "phrase1 "), 100),
                    Arguments.of(new ResponseFBSQ(1L, " phrase1 "), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase#1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase one"), 100)
            );
        }
    }

    @Nested
    public class NonCaseSensitiveNoTyposAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseFBSQ response, int expected) {
            QuestionFBSQDomain question = new QuestionFBSQDomain();
            question.setQuestionId(1L);
            AnswerFBSQDomain answer = new AnswerFBSQDomain();
            PhraseDomain phraseDomain0 = new PhraseDomain();
            phraseDomain0.setPhrase(ACCEPTED_PHRASES[0]);
            PhraseDomain phraseDomain1 = new PhraseDomain();
            phraseDomain1.setPhrase(ACCEPTED_PHRASES[1]);
            PhraseDomain phraseDomain2 = new PhraseDomain();
            phraseDomain2.setPhrase(ACCEPTED_PHRASES[2]);
            PhraseDomain[] phraseDomains = {phraseDomain0, phraseDomain1, phraseDomain2};
            answer.setAcceptedPhraseDomains(Set.of(phraseDomains));
            question.setAnswer(answer);
            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(false);
            settings.setTypoAllowed(false);
            answer.setSettings(settings);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBSQ(1L, null), 0),
                    Arguments.of((new ResponseFBSQ(1L, "")), 0),
                    Arguments.of((new ResponseFBSQ(1L, "000")), 0),
                    Arguments.of((new ResponseFBSQ(1L, "string1")), 0),
                    Arguments.of((new ResponseFBSQ(1L, "phrase 1")), 0),
                    Arguments.of((new ResponseFBSQ(1L, "phrase1 ")), 100),
                    Arguments.of((new ResponseFBSQ(1L, " phrase1 ")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "phrase1")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "phrase#1")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "phrase one")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "Phrase one")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "Phrase One")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "PHRASE one")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "Phrase ONE")), 100),
                    Arguments.of((new ResponseFBSQ(1L, "PHRASE ONE")), 100)
            );
        }
    }

    @Nested
    public class CaseSensitiveTyposAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseFBSQ response, int expected) {
            QuestionFBSQDomain question = new QuestionFBSQDomain();
            question.setQuestionId(1L);
            AnswerFBSQDomain answer = new AnswerFBSQDomain();
            PhraseDomain phraseDomain0 = new PhraseDomain();
            phraseDomain0.setPhrase(ACCEPTED_PHRASES[0]);
            PhraseDomain phraseDomain1 = new PhraseDomain();
            phraseDomain1.setPhrase(ACCEPTED_PHRASES[1]);
            PhraseDomain phraseDomain2 = new PhraseDomain();
            phraseDomain2.setPhrase(ACCEPTED_PHRASES[2]);
            answer.setAcceptedPhraseDomains(new HashSet<>(Arrays.asList(phraseDomain0, phraseDomain1, phraseDomain2)));
            question.setAnswer(answer);
            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(true);
            settings.setTypoAllowed(true);
            answer.setSettings(settings);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBSQ(1L, null), 0),
                    Arguments.of(new ResponseFBSQ(1L, ""), 0),
                    Arguments.of(new ResponseFBSQ(1L, "000"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "string1"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "phrase1 "), 100),
                    Arguments.of(new ResponseFBSQ(1L, " phrase1 "), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase#1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase one"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phraseone"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase on1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "prase one"), 100)
            );
        }
    }

    @Nested
    public class NonCaseSensitiveTyposAllowedTest {

        @ParameterizedTest
        @MethodSource("data")
        public void evaluateTest(ResponseFBSQ response, int expected) {
            QuestionFBSQDomain question = new QuestionFBSQDomain();
            question.setQuestionId(1L);
            AnswerFBSQDomain answer = new AnswerFBSQDomain();
            PhraseDomain phraseDomain0 = new PhraseDomain();
            phraseDomain0.setPhrase(ACCEPTED_PHRASES[0]);
            PhraseDomain phraseDomain1 = new PhraseDomain();
            phraseDomain1.setPhrase(ACCEPTED_PHRASES[1]);
            PhraseDomain phraseDomain2 = new PhraseDomain();
            phraseDomain2.setPhrase(ACCEPTED_PHRASES[2]);
            answer.setAcceptedPhraseDomains(new HashSet<>(Arrays.asList(phraseDomain0, phraseDomain1, phraseDomain2)));
            question.setAnswer(answer);
            SettingsFBDomain settings = new SettingsFBDomain();
            settings.setCaseSensitive(false);
            settings.setTypoAllowed(true);
            answer.setSettings(settings);
            assertEquals(expected, question.evaluate(response), 0.01, "Calculated score is not equal to the expected");
        }

        public static Stream<Arguments> data() {
            return Stream.of(
                    Arguments.of(new ResponseFBSQ(1L, null), 0),
                    Arguments.of(new ResponseFBSQ(1L, ""), 0),
                    Arguments.of(new ResponseFBSQ(1L, "000"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "string1"), 0),
                    Arguments.of(new ResponseFBSQ(1L, "phrase1 "), 100),
                    Arguments.of(new ResponseFBSQ(1L, " phrase1 "), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase#1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase one"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phraseone"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase on1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "prase one"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "Phraseone"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "phrase ON1"), 100),
                    Arguments.of(new ResponseFBSQ(1L, "PRASE ONE"), 100)
            );
        }
    }
}

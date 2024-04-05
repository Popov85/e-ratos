package ua.edu.ratos.service.session.sequence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.edu.ratos.dao.entity.QuestionType;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.dao.entity.question.QuestionMCQ;
import ua.edu.ratos.service.utils.CollectionShuffler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RequiredAwareSubSetProducerTest
        .RequiredAwareSubSetProducerTestContextConfiguration.class)
public class RequiredAwareSubSetProducerTest {

    @Autowired
    private RequiredAwareSubSetProducer requiredAwareSubSetProducer;

    @TestConfiguration
    static class RequiredAwareSubSetProducerTestContextConfiguration {

        @Bean
        public CollectionShuffler collectionShuffler() {
            return new CollectionShuffler();
        }

        @Bean
        public RequiredAwareSubSetProducer requiredAwareSubSetProducer() {
            return new RequiredAwareSubSetProducer(collectionShuffler());
        }
    }

    @Test
    public void getSubSetWithRequiredMinorityTest() {
        // Given total 10 questions of some type and level, 2 are required, requested 5
        Set<Question> sampleSet = new HashSet<>(Arrays.asList(
                createOne(1L, (byte) 1, "MCQ #1", true),
                createOne(2L, (byte) 1, "MCQ #2", false),
                createOne(3L, (byte) 1, "MCQ #3", false),
                createOne(4L, (byte) 1, "MCQ #4", false),
                createOne(5L, (byte) 1, "MCQ #5", false),
                createOne(6L, (byte) 1, "MCQ #6", false),
                createOne(7L, (byte) 1, "MCQ #7", false),
                createOne(8L, (byte) 1, "MCQ #8", false),
                createOne(9L, (byte) 1, "MCQ #9", false),
                createOne(10L, (byte) 1, "MCQ #10", true)));

        // Actual test begins
        List<Question> result = requiredAwareSubSetProducer.getSubSetWithRequired((short) 5, sampleSet);
        List<Question> required = result.stream().filter(Question::isRequired).collect(Collectors.toList());
        List<String> requiredStr = required.stream().map(Question::getQuestion).collect(Collectors.toList());

        assertThat("Resulting questions list is not of size = 5", result, hasSize(5));
        assertThat("Resulting required questions list is not of size = 2", required, hasSize(2));
        assertThat("Resulting required questions list does not contain all expected items", requiredStr, containsInAnyOrder("MCQ #1", "MCQ #10"));
    }

    @Test
    public void getSubSetWithRequiredMajorityTest() {
        // Given total 10 questions of some type and level, 9 (majority) are required, requested 5
        Set<Question> sampleSet = new HashSet<>(Arrays.asList(
                createOne(1L, (byte) 1, "MCQ #1", true),
                createOne(2L, (byte) 1, "MCQ #2", true),
                createOne(3L, (byte) 1, "MCQ #3", true),
                createOne(4L, (byte) 1, "MCQ #4", true),
                createOne(5L, (byte) 1, "MCQ #5", true),
                createOne(6L, (byte) 1, "MCQ #6", true),
                createOne(7L, (byte) 1, "MCQ #7", true),
                createOne(8L, (byte) 1, "MCQ #8", true),
                createOne(9L, (byte) 1, "MCQ #9", true),
                createOne(10L, (byte) 1, "MCQ #10", false)));

        // Actual test begins
        List<Question> result = requiredAwareSubSetProducer.getSubSetWithRequired((short) 5, sampleSet);
        List<Question> required = result.stream().filter(Question::isRequired).collect(Collectors.toList());
        List<String> requiredStr = result.stream().map(Question::getQuestion).collect(Collectors.toList());

        assertThat("Resulting questions list is not of size = 5", result, hasSize(5));
        assertThat("Resulting required questions list is not of size = 5", required, hasSize(5));
        assertThat("Resulting question list must not contain non-required item here", requiredStr, not(hasItem("MCQ #10")));
    }

    @Test
    public void getSubSetWithRequestedAllRequiredTest() {
        // Given total 10 questions of some type and level, 5 (all requested) are required, requested 5
        Set<Question> sampleSet = new HashSet<>(Arrays.asList(
                createOne(1L, (byte) 1, "MCQ #1", true),
                createOne(2L, (byte) 1, "MCQ #2", true),
                createOne(3L, (byte) 1, "MCQ #3", false),
                createOne(4L, (byte) 1, "MCQ #4", false),
                createOne(5L, (byte) 1, "MCQ #5", false),
                createOne(6L, (byte) 1, "MCQ #6", false),
                createOne(7L, (byte) 1, "MCQ #7", false),
                createOne(8L, (byte) 1, "MCQ #8", true),
                createOne(9L, (byte) 1, "MCQ #9", true),
                createOne(10L, (byte) 1, "MCQ #10", true)));

        // Actual test begins
        List<Question> result = requiredAwareSubSetProducer.getSubSetWithRequired((short) 5, sampleSet);
        List<Question> required = result.stream().filter(Question::isRequired).collect(Collectors.toList());
        List<String> requiredStr = required.stream().map(Question::getQuestion).collect(Collectors.toList());

        assertThat("Resulting questions list is not of size = 5", result, hasSize(5));
        assertThat("Resulting required questions list is not of size = 5", required, hasSize(5));
        assertThat("Resulting required questions list does not contain all expected items", requiredStr, containsInAnyOrder("MCQ #1", "MCQ #2", "MCQ #8", "MCQ #9", "MCQ #10"));
    }

    @Test
    public void getSubSetWithRequiredSingleTest() {
        // Given total 10 questions of some type and level, single requested (required) MCQ #5
        Set<Question> sampleSet = new HashSet<>(Arrays.asList(
                createOne(1L, (byte) 1, "MCQ #1", false),
                createOne(2L, (byte) 1, "MCQ #2", false),
                createOne(3L, (byte) 1, "MCQ #3", false),
                createOne(4L, (byte) 1, "MCQ #4", false),
                createOne(5L, (byte) 1, "MCQ #5", true),
                createOne(6L, (byte) 1, "MCQ #6", false),
                createOne(7L, (byte) 1, "MCQ #7", false),
                createOne(8L, (byte) 1, "MCQ #8", false),
                createOne(9L, (byte) 1, "MCQ #9", false),
                createOne(10L, (byte) 1, "MCQ #10", false)));

        // Actual test begins
        List<Question> result = requiredAwareSubSetProducer.getSubSetWithRequired((short) 1, sampleSet);
        List<Question> required = result.stream().filter(Question::isRequired).collect(Collectors.toList());
        List<String> requiredStr = required.stream().map(Question::getQuestion).collect(Collectors.toList());

        assertThat("Resulting questions list is not of size = 1", result, hasSize(1));
        assertThat("Resulting required questions list is not of size = 1", required, hasSize(1));
        assertThat("Resulting required questions list does not contain exactly one required item", requiredStr, containsInAnyOrder("MCQ #5"));
    }

    @Test
    public void getSubSetWithZeroRequestedTest() {
        // Given total 10 questions of some type and level, zero requested
        Set<Question> sampleSet = new HashSet<>(Arrays.asList(
                createOne(1L, (byte) 1, "MCQ #1", false),
                createOne(2L, (byte) 1, "MCQ #2", false),
                createOne(3L, (byte) 1, "MCQ #3", false),
                createOne(4L, (byte) 1, "MCQ #4", false),
                createOne(5L, (byte) 1, "MCQ #5", false),
                createOne(6L, (byte) 1, "MCQ #6", false),
                createOne(7L, (byte) 1, "MCQ #7", false),
                createOne(8L, (byte) 1, "MCQ #8", false),
                createOne(9L, (byte) 1, "MCQ #9", false),
                createOne(10L, (byte) 1, "MCQ #10", false)));

        // Actual test begins
        List<Question> result = requiredAwareSubSetProducer.getSubSetWithRequired((short) 0, sampleSet);
        List<Question> required = result.stream().filter(Question::isRequired).collect(Collectors.toList());

        assertThat("Resulting questions list is not empty", result, empty());
        assertThat("Resulting required questions list is not empty", required, empty());
    }

    private QuestionMCQ createOne(Long id, byte level, String question, boolean required) {
        QuestionMCQ q = new QuestionMCQ();
        q.setQuestionId(id);
        q.setQuestion(question);
        QuestionType type = new QuestionType();
        type.setTypeId(1L);
        q.setType(type);
        q.setLevel(level);
        q.setRequired(required);
        return q;
    }

}

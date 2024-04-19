package ua.edu.ratos.service.session.sequence._it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.dao.repository.SchemeRepository;
import ua.edu.ratos.service.session.sequence.Affiliation;
import ua.edu.ratos.service.session.sequence.QuestionLoader;
import ua.edu.ratos.service.session.sequence.QuestionLoaderFactory;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * Get Scheme 1L
 * Load questions to map
 * Check what map contains.
 */
@SpringBootTest
@Import(TestContainerConfig.class)
public class QuestionLoaderTestIT extends BaseIT {

    @Autowired
    private QuestionLoaderFactory questionLoaderFactory;

    @Autowired
    private SchemeRepository schemeRepository;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_all_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void loadAllQuestionsToMapWithSimpleImplTest() {
        Scheme scheme = schemeRepository.findForSessionById(1L)
                .orElseThrow(() -> new AssertionError("Scheme not found"));
        QuestionLoader simpleQuestionLoader = questionLoaderFactory.getInstance("simple");
        Map<Affiliation, Set<Question>> result = simpleQuestionLoader.loadAllQuestionsToMap(scheme);
        doAsserts(result);
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_all_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void loadAllQuestionsToMapWithCachedImplTest() {
        Scheme scheme = schemeRepository.findForSessionById(1L)
                .orElseThrow(() -> new AssertionError("Scheme not found"));
        QuestionLoader simpleQuestionLoader = questionLoaderFactory.getInstance("cached");
        Map<Affiliation, Set<Question>> result = simpleQuestionLoader.loadAllQuestionsToMap(scheme);
        doAsserts(result);
    }


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/questions_all_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void loadAllQuestionsToMapWithThreadLimitedImplTest() {
        Scheme scheme = schemeRepository.findForSessionById(1L)
                .orElseThrow(() -> new AssertionError("Scheme not found"));
        QuestionLoader simpleQuestionLoader = questionLoaderFactory.getInstance("thread-limited");
        Map<Affiliation, Set<Question>> result = simpleQuestionLoader.loadAllQuestionsToMap(scheme);
        doAsserts(result);
    }


    private void doAsserts(Map<Affiliation, Set<Question>> result) {

        Affiliation key1 = new Affiliation(1L, 1L, (byte) 1, (short) 5);
        Set<Question> questions1 = result.get(key1);

        Affiliation key2 = new Affiliation(2L, 2L, (byte) 1, (short) 5);
        Set<Question> questions2 = result.get(key2);

        Affiliation key3 = new Affiliation(3L, 3L, (byte) 1, (short) 5);
        Set<Question> questions3 = result.get(key3);

        Affiliation key4 = new Affiliation(4L, 4L, (byte) 1, (short) 5);
        Set<Question> questions4 = result.get(key4);

        Affiliation key5 = new Affiliation(5L, 5L, (byte) 1, (short) 5);
        Set<Question> questions5 = result.get(key5);

        assertThat("Resulting map size is not 5", result.size(), equalTo(5));
        assertThat("Collection of questions of type 1 is not of size 10", questions1, hasSize(10));
        assertThat("Collection of questions of type 2 is not of size 10", questions2, hasSize(10));
        assertThat("Collection of questions of type 3 is not of size 10", questions3, hasSize(10));
        assertThat("Collection of questions of type 4 is not of size 10", questions4, hasSize(10));
        assertThat("Collection of questions of type 5 is not of size 10", questions5, hasSize(10));
    }
}

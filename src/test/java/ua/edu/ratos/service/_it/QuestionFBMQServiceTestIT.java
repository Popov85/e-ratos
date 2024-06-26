package ua.edu.ratos.service._it;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.BaseIT;
import ua.edu.ratos.TestContainerConfig;
import ua.edu.ratos.dao.entity.question.QuestionFBMQ;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionFBMQInDto;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@Import(TestContainerConfig.class)
public class QuestionFBMQServiceTestIT extends BaseIT {

    public static final String JSON_NEW = "classpath:json/question_fbmq_in_dto_new.json";

    public static final String FIND = "select q from QuestionFBMQ q join fetch q.answers left join fetch q.helps join fetch q.resources where q.questionId=:questionId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbmq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        QuestionFBMQInDto dto = objectMapper.readValue(json, QuestionFBMQInDto.class);
        // Actual test begins
        questionService.save(dto);
        final QuestionFBMQ question = (QuestionFBMQ) em.createQuery(FIND).setParameter("questionId", 1L).getSingleResult();
        assertThat("Found Question FBMQ object is not as expected", question, allOf(
                hasProperty("questionId", equalTo(1L)),
                hasProperty("question", equalTo("Question #1")),
                hasProperty("resources", hasSize(1)),
                hasProperty("helps", hasSize(1)),
                hasProperty("answers", hasSize(3))
        ));
    }
}

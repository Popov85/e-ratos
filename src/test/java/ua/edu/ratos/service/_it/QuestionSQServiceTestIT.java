package ua.edu.ratos.service._it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.ActiveProfile;
import ua.edu.ratos.dao.entity.question.QuestionSQ;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionSQInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionSQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/question_sq_in_dto_new.json";

    public static final String FIND = "select q from QuestionSQ q join fetch q.answers left join fetch q.helps left join fetch q.resources where q.questionId=:questionId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_sq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        QuestionSQInDto dto = objectMapper.readValue(json, QuestionSQInDto.class);
        // Actual test begins
        questionService.save(dto);
        final QuestionSQ question = (QuestionSQ) em.createQuery(FIND).setParameter("questionId", 1L).getSingleResult();
        assertThat("Found Question object is not as expected", question, allOf(
                hasProperty("questionId", equalTo(1L)),
                hasProperty("question", equalTo("Question #1")),
                hasProperty("resources", hasSize(1)),
                hasProperty("helps", hasSize(1)),
                hasProperty("answers", hasSize(4))
        ));
    }
}

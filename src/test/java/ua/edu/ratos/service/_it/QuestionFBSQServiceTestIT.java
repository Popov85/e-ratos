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
import ua.edu.ratos.dao.entity.question.QuestionFBSQ;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionFBSQInDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionFBSQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/question_fbsq_in_dto_new.json";

    public static final String FIND = "select q from QuestionFBSQ q join fetch q.answer left join fetch q.helps left join fetch q.resources where q.questionId=:questionId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_fbsq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        QuestionFBSQInDto dto = objectMapper.readValue(json, QuestionFBSQInDto.class);
        // Actual test begins
        questionService.save(dto);
        final QuestionFBSQ question = (QuestionFBSQ) em.createQuery(FIND).setParameter("questionId", 1L).getSingleResult();
        assertThat("Found Question object is not as expected", question, allOf(
                hasProperty("questionId", equalTo(1L)),
                hasProperty("question", equalTo("Question #1")),
                hasProperty("resources", hasSize(1)),
                hasProperty("helps", hasSize(1)),
                hasProperty("answer", notNullValue())
        ));
    }
}

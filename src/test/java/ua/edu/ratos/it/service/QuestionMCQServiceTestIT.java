package ua.edu.ratos.it.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import ua.edu.ratos.domain.entity.question.QuestionMultipleChoice;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.entity.QuestionMCQInDto;
import javax.persistence.EntityManager;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class QuestionMCQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/question_mcq_in_dto_new.json";
    public static final String QUESTION_NEW = "Interface used to interact with the second-level cache";

    public static final String FIND = "select q from QuestionMultipleChoice q join fetch q.answers left join fetch q.help left join fetch q.resources where q.questionId=:questionId";

    @Autowired
    private QuestionService questionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "/scripts/question_mcq_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/question_mcq_test_clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        QuestionMCQInDto dto = objectMapper
                .readValue(json, QuestionMCQInDto.class);
        questionService.save(dto);
        final QuestionMultipleChoice foundQuestion =
            (QuestionMultipleChoice) em.createQuery(FIND)
                .setParameter("questionId",1L)
                .getSingleResult();
        Assert.assertNotNull(foundQuestion);
        Assert.assertEquals(QUESTION_NEW, foundQuestion.getQuestion());
        Assert.assertEquals(4, foundQuestion.getAnswers().size());
        Assert.assertEquals(1, foundQuestion.getHelp().get().size());
        Assert.assertEquals(0, foundQuestion.getResources().get().size());
    }

}

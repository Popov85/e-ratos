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
import ua.edu.ratos.dao.entity.question.QuestionSQ;
import ua.edu.ratos.it.ActiveProfile;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionSQInDto;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class QuestionSQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/question_sq_in_dto_new.json";

    public static final String QUESTION_NEW = "Question #1";

    public static final String FIND = "select q from QuestionSQ q join fetch q.answers left join fetch q.helps left join fetch q.resources where q.questionId=:questionId";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/question_sq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        QuestionSQInDto dto = objectMapper.readValue(json, QuestionSQInDto.class);
        questionService.save(dto);
        final QuestionSQ foundQuestion = (QuestionSQ) em.createQuery(FIND).setParameter("questionId", 1L).getSingleResult();
        Assert.assertNotNull(foundQuestion);
        Assert.assertEquals(QUESTION_NEW, foundQuestion.getQuestion());
        Assert.assertEquals(4, foundQuestion.getAnswers().size());
        Assert.assertTrue(foundQuestion.getHelp().isPresent());
        Assert.assertEquals(1, foundQuestion.getResources().size());
    }
}

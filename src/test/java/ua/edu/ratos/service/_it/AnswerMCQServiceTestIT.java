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
import ua.edu.ratos.dao.entity.answer.AnswerMCQ;
import ua.edu.ratos.service.AnswerMCQService;
import ua.edu.ratos.service.dto.in.AnswerMCQInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AnswerMCQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/answer_mcq_in_dto_new.json";
    public static final String JSON_UPD = "classpath:json/answer_mcq_in_dto_upd.json";

    public static final String FIND = "select a from AnswerMCQ a left join fetch a.resources where a.answerId=:answerId";
    public static final String ANSWER_NEW = "Answer #1";
    public static final String ANSWER_UPD = "Updated answer #1";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AnswerMCQService answerService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_mcq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        AnswerMCQInDto dto = objectMapper.readValue(json, AnswerMCQInDto.class);
        // Actual test begins
        answerService.save(1L, dto);
        final AnswerMCQ answer = (AnswerMCQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Saved AnswerMCQ object is not as expected", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("answer", equalTo(ANSWER_NEW)),
                hasProperty("percent", equalTo((short) 100)),
                hasProperty("required", equalTo(true)),
                hasProperty("resource", hasProperty("present", equalTo(true)))
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_mcq_test_data.sql", "/scripts/answer_mcq_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        AnswerMCQInDto dto = objectMapper.readValue(json, AnswerMCQInDto.class);
        // Actual test begins
        answerService.update(1L, dto);
        final AnswerMCQ answer = (AnswerMCQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Saved AnswerMCQ object is not as expected", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("answer", equalTo(ANSWER_UPD)),
                hasProperty("percent", equalTo((short) 50)),
                hasProperty("required", equalTo(false)),
                hasProperty("resource", hasProperty("present", equalTo(false)))
        ));
    }
}

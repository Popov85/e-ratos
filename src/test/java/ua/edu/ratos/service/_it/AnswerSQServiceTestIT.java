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
import ua.edu.ratos.dao.entity.answer.AnswerSQ;
import ua.edu.ratos.service.AnswerSQService;
import ua.edu.ratos.service.dto.in.AnswerSQInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AnswerSQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/answer_sq_in_dto_new.json";
    public static final String JSON_UPD = "classpath:json/answer_sq_in_dto_upd.json";

    public static final String FIND = "select a from AnswerSQ a where a.answerId=:answerId";
    public static final String PHRASE = "phrase";
    public static final String PHRASE_UPD = "updated phrase";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AnswerSQService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_sq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        AnswerSQInDto dto = objectMapper.readValue(json, AnswerSQInDto.class);
        // Actual test begins
        answerService.save(1L, dto);
        final AnswerSQ answer = (AnswerSQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Saved AnswerSQ object is not as expected", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("phrase", hasProperty("phrase", equalTo(PHRASE))),
                hasProperty("order", equalTo((short) 0))
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_sq_test_data.sql", "/scripts/answer_sq_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        AnswerSQInDto dto = objectMapper.readValue(json, AnswerSQInDto.class);
        // Actual test begins
        answerService.update(1L, dto);
        final AnswerSQ answer = (AnswerSQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Saved AnswerSQ object is not as expected", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("phrase", hasProperty("phrase", equalTo(PHRASE_UPD))),
                hasProperty("order", equalTo((short) 1))
        ));
    }

}

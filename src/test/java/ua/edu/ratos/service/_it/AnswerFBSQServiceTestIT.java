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
import ua.edu.ratos.dao.entity.Phrase;
import ua.edu.ratos.dao.entity.answer.AnswerFBSQ;
import ua.edu.ratos.service.AnswerFBSQService;
import ua.edu.ratos.service.dto.in.AnswerFBSQInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AnswerFBSQServiceTestIT {
    public static final String JSON_UPD = "classpath:json/answer_fbsq_in_dto_upd.json";

    public static final String FIND = "select a from AnswerFBSQ a join fetch a.acceptedPhrases where a.answerId=:answerId";
    public static final String PHRASE1 = "Phrase #1";
    public static final String PHRASE2 = "Phrase #2";
    public static final String PHRASE3_UPD = "Phrase #3";
    public static final long SETTINGS_ID_UPD = 2L;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AnswerFBSQService answerService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_fbsq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        AnswerFBSQInDto dto = objectMapper.readValue(json, AnswerFBSQInDto.class);
        // Actual test begins
        answerService.update(dto);
        final AnswerFBSQ answer = (AnswerFBSQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Updated AnswerFBSQ object is not equal", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("question", is(notNullValue())),
                hasProperty("settings", hasProperty("settingsId", equalTo(SETTINGS_ID_UPD))),
                hasProperty("acceptedPhrases", hasSize(3)),
                hasProperty("acceptedPhrases", contains(new Phrase(PHRASE1), new Phrase(PHRASE2), new Phrase(PHRASE3_UPD)))
        ));
    }
}

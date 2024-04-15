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
import ua.edu.ratos.dao.entity.answer.AnswerFBMQ;
import ua.edu.ratos.service.AnswerFBMQService;
import ua.edu.ratos.service.dto.in.AnswerFBMQInDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AnswerFBMQServiceTestIT {

    public static final String JSON_NEW = "classpath:json/answer_fbmq_in_dto_new.json";
    public static final String JSON_UPD = "classpath:json/answer_fbmq_in_dto_upd.json";

    public static final String FIND = "select a from AnswerFBMQ a left join fetch a.acceptedPhrases where a.answerId=:answerId";
    public static final String PHRASE = "Phrase";
    public static final String PHRASE_UPD = "Updated phrase";
    public static final String ACCEPTED_PHRASE1 = "Phrase #1";
    public static final String ACCEPTED_PHRASE2 = "Phrase #2";
    public static final String ACCEPTED_PHRASE3 = "Phrase #3";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AnswerFBMQService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_fbmq_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        AnswerFBMQInDto dto = objectMapper.readValue(json, AnswerFBMQInDto.class);
        // Actual test begins
        answerService.save(1L, dto);
        final AnswerFBMQ answer = (AnswerFBMQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Saved AnswerFBMQ object is not as expected", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("phrase", equalTo(PHRASE)),
                hasProperty("occurrence", equalTo((byte) 1)),
                hasProperty("settings", hasProperty("settingsId", equalTo(1L))),
                hasProperty("acceptedPhrases", hasSize(2)),
                hasProperty("acceptedPhrases",
                        containsInAnyOrder(new Phrase(ACCEPTED_PHRASE1), new Phrase(ACCEPTED_PHRASE2)))
        ));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_fbmq_test_data.sql", "/scripts/answer_fbmq_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_" + ActiveProfile.NOW + ".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        AnswerFBMQInDto dto = objectMapper.readValue(json, AnswerFBMQInDto.class);
        // Actual test begins
        answerService.update(1L, dto);
        final AnswerFBMQ answer = (AnswerFBMQ) em.createQuery(FIND).setParameter("answerId", 1L).getSingleResult();
        assertThat("Saved AnswerFBMQ object is not as expected", answer, allOf(
                hasProperty("answerId", equalTo(1L)),
                hasProperty("phrase", equalTo(PHRASE_UPD)),
                hasProperty("occurrence", equalTo((byte) 2)),
                hasProperty("settings", hasProperty("settingsId", equalTo(2L))),
                hasProperty("acceptedPhrases", hasSize(3)),
                hasProperty("acceptedPhrases",
                        containsInAnyOrder(new Phrase(ACCEPTED_PHRASE1), new Phrase(ACCEPTED_PHRASE2), new Phrase(ACCEPTED_PHRASE3)))
        ));
    }
}

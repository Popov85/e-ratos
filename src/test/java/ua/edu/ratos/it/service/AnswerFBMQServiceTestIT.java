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
import ua.edu.ratos.dao.entity.Phrase;
import ua.edu.ratos.dao.entity.answer.AnswerFBMQ;
import ua.edu.ratos.it.ActiveProfile;
import ua.edu.ratos.service.AnswerFBMQService;
import ua.edu.ratos.service.dto.in.AnswerFBMQInDto;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
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
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_NEW);
        AnswerFBMQInDto dto = objectMapper.readValue(json, AnswerFBMQInDto.class);
        answerService.save(1L, dto);
        final AnswerFBMQ foundAnswer = (AnswerFBMQ) em.createQuery(FIND).setParameter("answerId",1L).getSingleResult();
        Assert.assertNotNull(foundAnswer);
        Assert.assertEquals(PHRASE, foundAnswer.getPhrase());
        Assert.assertEquals(1, foundAnswer.getOccurrence());
        Assert.assertEquals(1, foundAnswer.getSettings().getSettingsId().longValue());
        Assert.assertTrue(foundAnswer.getAcceptedPhrases().contains(new Phrase(ACCEPTED_PHRASE1)));
        Assert.assertTrue(foundAnswer.getAcceptedPhrases().contains(new Phrase(ACCEPTED_PHRASE2)));
        Assert.assertFalse(foundAnswer.getAcceptedPhrases().contains(new Phrase(ACCEPTED_PHRASE3)));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_fbmq_test_data.sql", "/scripts/answer_fbmq_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateTest() throws Exception {
        File json = ResourceUtils.getFile(JSON_UPD);
        AnswerFBMQInDto dto = objectMapper.readValue(json, AnswerFBMQInDto.class);
        answerService.update(1L, dto);
        final AnswerFBMQ foundAnswer = (AnswerFBMQ) em.createQuery(FIND).setParameter("answerId",1L).getSingleResult();
        Assert.assertNotNull(foundAnswer);
        Assert.assertEquals(PHRASE_UPD, foundAnswer.getPhrase());
        Assert.assertEquals(2, foundAnswer.getOccurrence());
        Assert.assertEquals(2, foundAnswer.getSettings().getSettingsId().longValue());
        Assert.assertTrue(foundAnswer.getAcceptedPhrases().contains(new Phrase(ACCEPTED_PHRASE1)));
        Assert.assertTrue(foundAnswer.getAcceptedPhrases().contains(new Phrase(ACCEPTED_PHRASE2)));
        Assert.assertTrue(foundAnswer.getAcceptedPhrases().contains(new Phrase(ACCEPTED_PHRASE3)));
    }

    @Test
    @Sql(scripts = {"/scripts/init.sql", "/scripts/answer_fbmq_test_data.sql", "/scripts/answer_fbmq_test_data_one.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/scripts/test_data_clear_"+ ActiveProfile.NOW+".sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteTest() {
        Assert.assertNotNull(em.find(AnswerFBMQ.class, 1L));
        answerService.deleteById(1L);
        Assert.assertNull(em.find(AnswerFBMQ.class, 1L));
    }

}

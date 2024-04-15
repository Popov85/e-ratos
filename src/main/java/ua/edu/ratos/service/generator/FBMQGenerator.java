package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.answer.AnswerFBMQ;
import ua.edu.ratos.dao.entity.question.QuestionFBMQ;
import ua.edu.ratos.dao.repository.QuestionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile({"dev"})
public class FBMQGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private Rnd rnd;

    @TrackTime
    @Transactional
    public List<QuestionFBMQ> generate(int quantity, List<Theme> themes, List<Resource> resources, List<Help> helps, List<Phrase> acceptedPhrases) {
        List<QuestionFBMQ> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Theme theme = themes.get(rnd.rnd(0, themes.size() - 1));
            Help help = helps.get(rnd.rnd(0, helps.size() - 1));
            QuestionFBMQ question = createOne(i, theme, help, resources, acceptedPhrases);
            result.add(question);
        }
        questionRepository.saveAll(result);
        return result;
    }

    private QuestionFBMQ createOne(int questionId, Theme theme, Help help, List<Resource> resources, List<Phrase> acceptedPhrases) {
        QuestionFBMQ questionFBMQ = new QuestionFBMQ();
        questionFBMQ.setQuestion("Question FBMQ, text: {phrase0, phrase1, phrase2} #"+questionId);
        questionFBMQ.setType(em.getReference(QuestionType.class, 3L));
        questionFBMQ.setLevel((byte) rnd.rnd(1, 4));
        questionFBMQ.setTheme(theme);

        // each 100th is deleted
        if (questionId%100==0) questionFBMQ.setDeleted(true);

        questionFBMQ.addHelp(em.getReference(Help.class, help.getHelpId()));

        Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        questionFBMQ.addResource(em.getReference(Resource.class, resource.getResourceId()));

        int quantity = this.rnd.rnd(1, 4);
        for (int i = 0; i <quantity ; i++) {
            questionFBMQ.addAnswer(createAnswer(i, acceptedPhrases));
        }
        return questionFBMQ;
    }

    private AnswerFBMQ createAnswer(int index, List<Phrase> acceptedPhrases) {
        AnswerFBMQ answer = new AnswerFBMQ();
        answer.setPhrase("phrase"+index);
        answer.setOccurrence((byte)1);
        answer.setSettings(em.getReference(SettingsFB.class, 1L));
        answer.setAcceptedPhrases(createPhrases(3, acceptedPhrases));
        return answer;
    }

    private Set<Phrase> createPhrases(int quantity, List<Phrase> acceptedPhrases) {
        Set<Phrase> result = new HashSet<>();
        for (int i = 1; i <=quantity ; i++) {
            int index = this.rnd.rnd(0, acceptedPhrases.size() - 1);
            result.add(em.getReference(Phrase.class, acceptedPhrases.get(index).getPhraseId()));
        }
        return result;
    }

}

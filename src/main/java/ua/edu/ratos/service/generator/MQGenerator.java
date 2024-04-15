package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.answer.AnswerMQ;
import ua.edu.ratos.dao.entity.question.QuestionMQ;
import ua.edu.ratos.dao.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev"})
public class MQGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private Rnd rnd;

    @TrackTime
    @Transactional
    public List<QuestionMQ> generate(int quantity, List<Theme> themes, List<Resource> resources, List<Help> helps, List<Phrase> acceptedPhrases) {
        List<QuestionMQ> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Theme theme = themes.get(rnd.rnd(0, themes.size() - 1));
            Help help = helps.get(rnd.rnd(0, helps.size() - 1));
            QuestionMQ question = createOne(i, theme, help, resources, acceptedPhrases);
            result.add(question);
        }
        questionRepository.saveAll(result);
        return result;
    }

    private QuestionMQ createOne(int questionId, Theme theme, Help help, List<Resource> resources, List<Phrase> acceptedPhrases) {
        QuestionMQ questionMQ = new QuestionMQ();
        questionMQ.setQuestion("Question MQ #"+questionId);
        questionMQ.setType(em.getReference(QuestionType.class, 4L));
        questionMQ.setLevel((byte) rnd.rnd(1, 4));
        questionMQ.setTheme(theme);

        // each 100th is deleted
        if (questionId%100==0) questionMQ.setDeleted(true);

        questionMQ.addHelp(em.getReference(Help.class, help.getHelpId()));

        Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        questionMQ.addResource(em.getReference(Resource.class, resource.getResourceId()));

        int quantity = this.rnd.rnd(3, 6);
        for (int i = 0; i <quantity ; i++) {
            questionMQ.addAnswer(createAnswer(acceptedPhrases));
        }
        return questionMQ;
    }

    private AnswerMQ createAnswer(List<Phrase> acceptedPhrases) {
        if (acceptedPhrases.size()<3)
            throw new IllegalStateException("AcceptedPhrases list size too small <3");
        AnswerMQ answer = new AnswerMQ();
        int left = this.rnd.rnd(0, acceptedPhrases.size() - 2);
        answer.setLeftPhrase(em.getReference(Phrase.class, acceptedPhrases.get(left).getPhraseId()));
        answer.setRightPhrase(em.getReference(Phrase.class, acceptedPhrases.get(left+1).getPhraseId()));
        return answer;
    }

}

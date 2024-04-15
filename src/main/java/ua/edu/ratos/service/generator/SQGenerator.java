package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.answer.AnswerSQ;
import ua.edu.ratos.dao.entity.question.QuestionSQ;
import ua.edu.ratos.dao.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev"})
public class SQGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private Rnd rnd;

    @TrackTime
    @Transactional
    public List<QuestionSQ> generate(int quantity, List<Theme> themes, List<Resource> resources, List<Help> helps, List<Phrase> acceptedPhrases) {
        List<QuestionSQ> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Theme theme = themes.get(rnd.rnd(0, themes.size() - 1));
            Help help = helps.get(rnd.rnd(0, helps.size() - 1));
            QuestionSQ question = createOne(i, theme, help, resources, acceptedPhrases);
            result.add(question);
        }
        questionRepository.saveAll(result);
        return result;
    }

    private QuestionSQ createOne(int questionId, Theme theme, Help help, List<Resource> resources, List<Phrase> acceptedPhrases) {
        QuestionSQ questionSQ = new QuestionSQ();
        questionSQ.setQuestion("Question SQ #"+questionId);
        questionSQ.setType(em.getReference(QuestionType.class, 5L));
        questionSQ.setLevel((byte) rnd.rnd(1, 4));
        questionSQ.setTheme(theme);

        // each 100th is deleted
        if (questionId%100==0) questionSQ.setDeleted(true);

        questionSQ.addHelp(em.getReference(Help.class, help.getHelpId()));

        Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        questionSQ.addResource(em.getReference(Resource.class, resource.getResourceId()));

        int quantity = this.rnd.rnd(3, 7);
        for (int i = 1; i <= quantity ; i++) {
            questionSQ.addAnswer(createAnswer(i, acceptedPhrases));
        }
        return questionSQ;
    }

    private AnswerSQ createAnswer(int order, List<Phrase> acceptedPhrases) {
        if (acceptedPhrases.size()<4)
            throw new IllegalStateException("AcceptedPhrases list size too small <4");
        AnswerSQ answer = new AnswerSQ();
        int index = this.rnd.rnd(0, acceptedPhrases.size() - 1);
        answer.setPhrase(em.getReference(Phrase.class, acceptedPhrases.get(index).getPhraseId()));
        answer.setOrder((short)order);
        return answer;
    }

}

package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.answer.AnswerFBSQ;
import ua.edu.ratos.dao.entity.question.QuestionFBSQ;
import ua.edu.ratos.dao.repository.QuestionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile({"dev"})
public class FBSQGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private Rnd rnd;

    @TrackTime
    @Transactional
    public List<QuestionFBSQ> generate(int quantity, List<Theme> themes, List<Resource> resources, List<Help> helps, List<Phrase> acceptedPhrases) {
        List<QuestionFBSQ> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Theme theme = themes.get(rnd.rnd(0, themes.size() - 1));
            Help help = helps.get(rnd.rnd(0, helps.size() - 1));
            QuestionFBSQ question = createOne(i, theme, help, resources, acceptedPhrases);
            result.add(question);
        }
        questionRepository.saveAll(result);
        return result;
    }

    private QuestionFBSQ createOne(int questionId, Theme theme, Help help, List<Resource> resources, List<Phrase> acceptedPhrases) {
        QuestionFBSQ questionFBSQ = new QuestionFBSQ();
        questionFBSQ.setQuestion("Question FBSQ #"+questionId);
        questionFBSQ.setType(em.getReference(QuestionType.class, 2L));
        questionFBSQ.setLevel((byte) rnd.rnd(1, 4));
        questionFBSQ.setTheme(theme);

        // each 100th is deleted
        if (questionId%100==0) questionFBSQ.setDeleted(true);

        questionFBSQ.addHelp(em.getReference(Help.class, help.getHelpId()));

        Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        questionFBSQ.addResource(em.getReference(Resource.class, resource.getResourceId()));

        int quantity = this.rnd.rnd(1, 6);
        questionFBSQ.addAnswer(createAnswer(createPhrases(quantity, acceptedPhrases)));

        return questionFBSQ;
    }

    private Set<Phrase> createPhrases(int quantity, List<Phrase> acceptedPhrases) {
        Set<Phrase> result = new HashSet<>();
        for (int i = 1; i <=quantity ; i++) {
            int index = this.rnd.rnd(0, acceptedPhrases.size() - 1);
            result.add(em.getReference(Phrase.class, acceptedPhrases.get(index).getPhraseId()));
        }
        return result;
    }

    private AnswerFBSQ createAnswer(Set<Phrase> acceptedPhrases) {
        AnswerFBSQ answer = new AnswerFBSQ();
        answer.setSettings(em.getReference(SettingsFB.class, 1L));
        answer.setAcceptedPhrases(acceptedPhrases);
        return answer;
    }

}

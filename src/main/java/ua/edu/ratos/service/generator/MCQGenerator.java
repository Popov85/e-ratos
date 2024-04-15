package ua.edu.ratos.service.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.answer.AnswerMCQ;
import ua.edu.ratos.dao.entity.question.QuestionMCQ;
import ua.edu.ratos.dao.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile({"dev"})
public class MCQGenerator {

    private static final int MAX_LEVEL = 1;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private Rnd rnd;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RealWordGenerator realWordGenerator;

    @Autowired
    private RealSentenceGenerator realSentenceGenerator;


    @TrackTime
    @Transactional
    public List<QuestionMCQ> generate(int quantity, List<Theme> themes, List<Resource> resources, List<Help> helps) {
        List<QuestionMCQ> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Theme theme = themes.get(rnd.rnd(0, themes.size()));
            Help help = helps.get(rnd.rnd(0, helps.size() - 1));
            QuestionMCQ question = createOne(i, theme, help, resources);
            result.add(question);
            if (i%1000==0) {
                questionRepository.saveAll(result);
                em.flush();
                result = new ArrayList<>();
                log.debug("Flushed another patch of MCQ, i = {}", i);
            }
        }
        questionRepository.saveAll(result);
        return result;
    }

    private QuestionMCQ createOne(int questionId, Theme theme, Help help, List<Resource> resources) {
        QuestionMCQ questionMCQ = new QuestionMCQ();
        // 50-150 words, 5-15 symbols per word
        String question = realSentenceGenerator.createText(4, 15,  true);
        questionMCQ.setQuestion(question);
        questionMCQ.setType(em.getReference(QuestionType.class, 1L));
        questionMCQ.setLevel((byte) rnd.rnd(1, MAX_LEVEL+1));
        questionMCQ.setTheme(theme);

        // each 100th is deleted
        if (questionId%100==0) questionMCQ.setDeleted(true);

        questionMCQ.addHelp(em.getReference(Help.class, help.getHelpId()));

        // Resource for every 5th question...
        int random = rnd.rnd(0, 4);
        if (random==2) {
            Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
            questionMCQ.addResource(em.getReference(Resource.class, resource.getResourceId()));
        }

        // Multi- or single?
        int type = rnd.rnd(0, 2);
        List<AnswerMCQ> answers;
        if (type ==0) {
            // single MCQ
            answers = createAnswers(questionId, rnd.rnd(3, 7), resources);
        } else {
           // multi MCQ
            answers = createMultiAnswers(questionId, rnd.rnd(3, 7), resources);
        }
        answers.forEach(a->questionMCQ.addAnswer(a));
        return questionMCQ;
    }

    private List<AnswerMCQ> createAnswers(int questionId, int quantity, List<Resource> resources) {
        List<AnswerMCQ> result = new ArrayList<>();
        for (int i = 1; i < quantity; i++) {
            // Add many incorrect answers
            result.add(createInCorrectAnswer(questionId, i, resources));
        }
        // At the end add the correct answer
        result.add(createCorrectAnswer(questionId, quantity, resources));
        return result;
    }

    private List<AnswerMCQ> createMultiAnswers(int questionId, int quantity, List<Resource> resources) {
        List<AnswerMCQ> result = new ArrayList<>();
        int multi = rnd.rnd(2, quantity> 5 ? 5 : quantity); // how many partially correct will be, no more than 5!

        // generate partially correct
        for (int i = 1; i <= multi ; i++) {
            switch (multi) {
                case 2: result.add(createPartiallyCorrectAnswer(questionId, i, resources, (short)50));
                break;
                case 3: {
                    if (i!=multi) {
                        result.add(createPartiallyCorrectAnswer(questionId, i, resources, (short)33));
                    } else {
                        result.add(createPartiallyCorrectAnswer(questionId, i, resources, (short)34));
                    }
                }
                break;
                case 4: result.add(createPartiallyCorrectAnswer(questionId, i, resources, (short)25));
                    break;
                case 5: result.add(createPartiallyCorrectAnswer(questionId, i, resources, (short)20));
                    break;
                default: result.add(createPartiallyCorrectAnswer(questionId, i, resources, (short)50));
            }
        }
        int incorrect = quantity - multi; // how many of incorrect will be
        for (int i = multi+1; i <= (multi+incorrect); i++) {
            // Add some incorrect answers
            result.add(createInCorrectAnswer(questionId, i, resources));
        }
        return result;
    }

    private AnswerMCQ createInCorrectAnswer(int questionId, int answer, List<Resource> resources) {
        AnswerMCQ answerMCQ = new AnswerMCQ();
        String text = realWordGenerator.createSentence(15, 30,  false);
        answerMCQ.setAnswer("(Incorrect) "+text);
        answerMCQ.setPercent((short)0);
        answerMCQ.setRequired(false);
        //Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        //answerMCQ.addResource(em.getReference(Resource.class, resource.getResourceId()));
        return answerMCQ;
    }

    private AnswerMCQ createCorrectAnswer(int questionId, int answer, List<Resource> resources) {
        AnswerMCQ answerMCQ = new AnswerMCQ();
        String text = realWordGenerator.createSentence(15, 30,  false);
        answerMCQ.setAnswer("(Correct) "+text);
        answerMCQ.setPercent((short)100);
        answerMCQ.setRequired(true);
        //Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        //answerMCQ.addResource(em.getReference(Resource.class, resource.getResourceId()));
        return answerMCQ;
    }

    private AnswerMCQ createPartiallyCorrectAnswer(int questionId, int answer, List<Resource> resources, short percent) {
        AnswerMCQ answerMCQ = new AnswerMCQ();
        String text = realWordGenerator.createSentence(15, 30,  false);
        answerMCQ.setAnswer("(Partially correct) "+text);
        answerMCQ.setPercent(percent);
        answerMCQ.setRequired(true);
        //Resource resource = resources.get(rnd.rnd(0, resources.size() - 1));
        //answerMCQ.addResource(em.getReference(Resource.class, resource.getResourceId()));
        return answerMCQ;
    }


    //------------------------------Extended version (new resource and help for each question)--------------------------


    @Transactional
    public List<QuestionMCQ> generate(int quantity, List<Theme> themes) {
        List<QuestionMCQ> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Theme theme = themes.get(rnd.rnd(0, themes.size() - 1));
            QuestionMCQ question = createOne(i, theme);
            result.add(question);
        }
        questionRepository.saveAll(result);
        return result;
    }

    private QuestionMCQ createOne(int questionId, Theme theme) {
        QuestionMCQ questionMCQ = new QuestionMCQ();
        questionMCQ.setQuestion("Question MCQ #"+questionId);
        questionMCQ.setType(em.getReference(QuestionType.class, 1L));
        questionMCQ.setLevel((byte) rnd.rnd(1, 3));
        questionMCQ.setTheme(theme);

        addHelp(questionMCQ);

        addResource(questionMCQ);

        List<AnswerMCQ> answers = createAnswers(questionId, rnd.rnd(3, 5), true);
        answers.forEach(a->questionMCQ.addAnswer(a));
        return questionMCQ;
    }


    private List<AnswerMCQ> createAnswers(int questionId, int quantity, boolean resource) {
        List<AnswerMCQ> result = new ArrayList<>();
        for (int i = 1; i < quantity; i++) {
            // Add many incorrect answers
            result.add(createInCorrectAnswer(questionId, i, resource));
        }
        // At the end add the correct answer
        result.add(createCorrectAnswer(questionId, quantity, resource));
        return result;
    }


    private AnswerMCQ createInCorrectAnswer(int questionId, int answer, boolean resource) {
        AnswerMCQ answerMCQ = new AnswerMCQ();
        answerMCQ.setAnswer("Answer (incorrect) #"+answer+" to question #"+questionId);
        answerMCQ.setPercent((short)0);
        answerMCQ.setRequired(false);
        if (resource) addResource(answerMCQ);
        return answerMCQ;
    }

    private AnswerMCQ createCorrectAnswer(int questionId, int answer, boolean resource) {
        AnswerMCQ answerMCQ = new AnswerMCQ();
        answerMCQ.setAnswer("Answer (correct) #"+answer+" to question #"+questionId);
        answerMCQ.setPercent((short)100);
        answerMCQ.setRequired(true);
        if (resource) addResource(answerMCQ);
        return answerMCQ;
    }

    private AnswerMCQ addResource(AnswerMCQ a) {
        Resource resource = new Resource();
        resource.setDescription("Resource for answer "+a.getAnswer());
        resource.setLink("https://resources-for-answers.com/"+a.getAnswer());
        resource.setStaff(em.getReference(Staff.class, 1L));
        a.addResource(resource);
        return a;
    }

    private QuestionMCQ addResource(QuestionMCQ q) {
        Resource resource = new Resource();
        resource.setDescription("Resource for question "+q.getQuestion());
        resource.setLink("https://resources-for-question.com/"+q.getQuestion());
        resource.setStaff(em.getReference(Staff.class, 1L));
        q.addResource(resource);
        return q;
    }

    private QuestionMCQ addHelp(QuestionMCQ q) {
        Help help = new Help();
        help.setName("Help MCQ for "+q.getQuestion());
        help.setHelp("See Help MCQ for "+q.getQuestion());
        help.setStaff(em.getReference(Staff.class, 1L));
        addResource(help);
        q.addHelp(help);
        return q;
    }

    private Help addResource(Help h) {
        Resource resource = new Resource();
        resource.setDescription("Resource for help "+h.getName());
        resource.setLink("https://resources-for-helps.com/"+h.getName());
        resource.setStaff(em.getReference(Staff.class, 1L));
        h.addResource(resource);
        return h;
    }
}

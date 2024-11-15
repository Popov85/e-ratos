package ua.edu.ratos.service.session;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.Strategy;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.session.sequence.QuestionLoader;
import ua.edu.ratos.service.session.sequence.SequenceFactory;
import ua.edu.ratos.service.session.sequence.SequenceProducer;
import ua.edu.ratos.service.transformer.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IndividualSequenceProducer {

    private final QuestionLoaderSelector questionLoaderSelector;

    private final SequenceFactory sequenceFactory;

    private final QuestionMapper questionMapper;

    /**
     * Prepares an individual sequence of questions for student's session enriched with serialNumber
     * @param scheme Scheme entity
     * @return individual sequence of questions
     */
    @Timed(value = "get_individual_sequence.timed", description = "Time taken to obtain individual sequence of questions")
    public List<QuestionDomain> getIndividualSequence(@NonNull final Scheme scheme) {
        QuestionLoader questionLoader = questionLoaderSelector.select(scheme);
        Strategy strategy = scheme.getStrategy();
        SequenceProducer sequenceProducer = sequenceFactory.getInstance(strategy.getName());
        List<Question> questions = sequenceProducer.getSequence(scheme, questionLoader);
        List<QuestionDomain> sequence = new ArrayList<>(questions.size());
        // Let's enumerate each questions in the sequence (for displaying in browser)
        for (int i = 0; i < questions.size(); i++) {
            QuestionDomain questionDomain = questionMapper.toDomain(questions.get(i));
            int serialNumber = i + 1;
            questionDomain.setSerialNumber(serialNumber);
            sequence.add(questionDomain);
        }
        log.debug("Produced the sequence of total = {} question entities, strategy = {}", questions.size(), strategy.getName());
        return sequence;
    }
}

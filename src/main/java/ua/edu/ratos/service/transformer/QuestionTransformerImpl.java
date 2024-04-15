package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.Theme;
import ua.edu.ratos.dao.entity.question.*;
import ua.edu.ratos.dao.repository.QuestionTypeRepository;
import ua.edu.ratos.service.dto.in.*;
import ua.edu.ratos.service.transformer.AnswerTransformer;
import ua.edu.ratos.service.transformer.QuestionTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;

@Component
@AllArgsConstructor
public class QuestionTransformerImpl implements QuestionTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final AnswerTransformer answerTransformer;

    private final QuestionTypeRepository questionTypeRepository;

    @Override
    public QuestionMCQ toEntity(@NonNull final QuestionMCQInDto dto) {
        QuestionMCQ question = new QuestionMCQ();
        mapDto(dto, question, "MCQ");
        dto.getAnswers().forEach(a -> question.addAnswer(answerTransformer.toEntity(a)));
        return question;
    }

    @Override
    public QuestionFBSQ toEntity(@NonNull final QuestionFBSQInDto dto) {
        check(dto.getAnswer().getPhrasesIds(), 1, "This question must contain at least one accepted phrase");
        QuestionFBSQ question = new QuestionFBSQ();
        mapDto(dto, question, "FBSQ");
        AnswerFBSQInDto answerDto = dto.getAnswer();
        question.addAnswer(answerTransformer.toEntity(answerDto));
        return question;
    }

    @Override
    public QuestionFBMQ toEntity(@NonNull final QuestionFBMQInDto dto) {
        check(dto.getAnswers(), 1, "This question must contain at least 1 answer");
        QuestionFBMQ question = new QuestionFBMQ();
        mapDto(dto, question, "FBMQ");
        dto.getAnswers().forEach(a -> question.addAnswer(answerTransformer.toEntity(a)));
        return question;
    }

    @Override
    public QuestionMQ toEntity(@NonNull final QuestionMQInDto dto) {
        check(dto.getAnswers(), 2, "This question must contain at least 2 answers");
        QuestionMQ question = new QuestionMQ();
        mapDto(dto, question, "MQ");
        dto.getAnswers().forEach(a -> question.addAnswer(answerTransformer.toEntity(a)));
        return question;
    }

    @Override
    public QuestionSQ toEntity(@NonNull final QuestionSQInDto dto) {
        check(dto.getAnswers(), 3, "This question must contain at least 3 answers");
        QuestionSQ question = new QuestionSQ();
        mapDto(dto, question, "SQ");
        dto.getAnswers().forEach(a -> question.addAnswer(answerTransformer.toEntity(a)));
        return question;
    }

    void mapDto(@NonNull final QuestionInDto dto, @NonNull final Question question, @NonNull final String questionType) {
        question.setQuestionId(dto.getQuestionId());
        question.setQuestion(dto.getQuestion());
        question.setLevel(dto.getLevel());
        question.setTheme(em.getReference(Theme.class, dto.getThemeId()));
        question.setType(questionTypeRepository.findTypeByAbbreviation(questionType)
                .orElseThrow(() -> new EntityNotFoundException("Failed to find QuestionType by its abbreviation")));
        question.setRequired(dto.isRequired());
        if (dto.getHelpId().isPresent()) {
            question.addHelp(em.find(Help.class, dto.getHelpId().get()));
        } else {
            question.clearHelps();
        }
        if (dto.getResourceId().isPresent()) {
            question.addResource(em.find(Resource.class, dto.getResourceId().get()));
        } else {
            question.clearResources();
        }
    }

    // TODO: consider move this functionality to the dedicated validators!
    private <T> void check(Collection<T> collection, int threshold, String message) {
        if (collection.isEmpty() || collection.size() < threshold)
            throw new IllegalStateException(message);
    }
}

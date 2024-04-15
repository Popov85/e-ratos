package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.answer.AnswerSQRepository;
import ua.edu.ratos.service.dto.in.AnswerSQInDto;
import ua.edu.ratos.service.transformer.AnswerTransformer;

import jakarta.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class AnswerSQService {

    private static final String ANSWER_NOT_FOUND = "The requested Answer SQ not found, answerId = ";

    private final AnswerSQRepository answerRepository;

    private final AnswerTransformer answerTransformer;

    @Transactional
    public Long save(@NonNull final Long questionId, @NonNull final AnswerSQInDto dto) {
        return answerRepository.save(answerTransformer.toEntity(questionId, dto)).getAnswerId();
    }

    @Transactional
    public void update(@NonNull final Long questionId, @NonNull final AnswerSQInDto dto) {
        if (dto.getAnswerId()==null) throw new RuntimeException();
        answerRepository.save(answerTransformer.toEntity(questionId, dto));
    }

    @Transactional
    public void deleteById(@NonNull final Long answerId) {
        answerRepository.findById(answerId).orElseThrow(()->new EntityNotFoundException(ANSWER_NOT_FOUND + answerId))
                .setDeleted(true);
    }
}

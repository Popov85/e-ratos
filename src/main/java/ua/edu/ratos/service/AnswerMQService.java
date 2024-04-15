package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.answer.AnswerMQRepository;
import ua.edu.ratos.service.dto.in.AnswerMQInDto;
import ua.edu.ratos.service.transformer.AnswerTransformer;

import jakarta.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class AnswerMQService {

    private static final String ANSWER_NOT_FOUND = "The requested Answer MQ not found, answerId = ";

    private final AnswerMQRepository answerRepository;

    private final AnswerTransformer answerTransformer;

    @Transactional
    public Long save(@NonNull final Long questionId, @NonNull final AnswerMQInDto dto) {
        return answerRepository.save(answerTransformer.toEntity(questionId, dto)).getAnswerId();
    }

    @Transactional
    public void update(@NonNull Long questionId, @NonNull final AnswerMQInDto dto) {
        if (dto.getAnswerId()==null) throw new RuntimeException();
        answerRepository.save(answerTransformer.toEntity(questionId, dto));
    }

    @Transactional
    public void deleteById(@NonNull final Long answerId) {
        answerRepository.findById(answerId).orElseThrow(()->new EntityNotFoundException(ANSWER_NOT_FOUND + answerId))
                .setDeleted(true);
    }

}

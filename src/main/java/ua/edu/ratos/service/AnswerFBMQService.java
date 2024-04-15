package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.answer.AnswerFBMQRepository;
import ua.edu.ratos.service.dto.in.AnswerFBMQInDto;
import ua.edu.ratos.service.transformer.AnswerTransformer;

import jakarta.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class AnswerFBMQService {

    private static final String ANSWER_NOT_FOUND = "The requested Answer FBMQ not found, answerId = ";

    private final AnswerFBMQRepository answerRepository;

    private final AnswerTransformer answerTransformer;


    @Transactional
    public Long save(@NonNull final Long questionId, @NonNull final AnswerFBMQInDto dto) {
        return answerRepository.save(answerTransformer.toEntity(questionId, dto)).getAnswerId();
    }

    @Transactional
    public void update(@NonNull final Long questionId, @NonNull AnswerFBMQInDto dto) {
        if (dto.getAnswerId()==null) throw new RuntimeException("Answer FBMQ must have answerId to be updated");
        answerRepository.save(answerTransformer.toEntity(questionId, dto));
    }

    @Transactional
    public void deleteById(@NonNull final Long answerId) {
        answerRepository.findById(answerId).orElseThrow(() -> new EntityNotFoundException(ANSWER_NOT_FOUND + answerId))
                .setDeleted(true);
    }

}

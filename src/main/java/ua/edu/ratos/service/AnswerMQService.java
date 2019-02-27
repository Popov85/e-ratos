package ua.edu.ratos.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.answer.AnswerMQRepository;
import ua.edu.ratos.service.dto.in.AnswerMQInDto;
import ua.edu.ratos.service.transformer.dto_to_entity.DtoAnswerTransformer;
import javax.persistence.EntityNotFoundException;

@Service
public class AnswerMQService {

    private static final String ANSWER_NOT_FOUND = "The requested Answer MQ not found, answerId = ";

    private AnswerMQRepository answerRepository;

    private DtoAnswerTransformer dtoAnswerTransformer;

    @Autowired
    public void setAnswerRepository(AnswerMQRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Autowired
    public void setDtoAnswerTransformer(DtoAnswerTransformer dtoAnswerTransformer) {
        this.dtoAnswerTransformer = dtoAnswerTransformer;
    }



    @Transactional
    public Long save(@NonNull final Long questionId, @NonNull final AnswerMQInDto dto) {
        return answerRepository.save(dtoAnswerTransformer.toEntity(questionId, dto)).getAnswerId();
    }

    @Transactional
    public void update(@NonNull Long questionId, @NonNull final AnswerMQInDto dto) {
        if (dto.getAnswerId()==null) throw new RuntimeException();
        answerRepository.save(dtoAnswerTransformer.toEntity(questionId, dto));
    }

    @Transactional
    public void deleteById(@NonNull final Long answerId) {
        answerRepository.findById(answerId).orElseThrow(()->new EntityNotFoundException(ANSWER_NOT_FOUND + answerId))
                .setDeleted(true);
    }

}

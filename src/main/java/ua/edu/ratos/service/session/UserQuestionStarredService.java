package ua.edu.ratos.service.session;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.User;
import ua.edu.ratos.dao.entity.UserQuestionStarred;
import ua.edu.ratos.dao.entity.UserQuestionStarredId;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.dao.repository.UserQuestionStarredRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.session.question.QuestionSessionMinOutDto;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;
import ua.edu.ratos.service.transformer.UserQuestionStarredTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserQuestionStarredService {

    private static final String ENTITY_NOT_FOUND = "Failed to find starred question by ID";

    @PersistenceContext
    private final EntityManager em;

    private final UserQuestionStarredRepository userQuestionStarredRepository;

    private final UserQuestionStarredTransformer userQuestionStarredTransformer;

    private final SecurityUtils securityUtils;

    //--------------------------------------------------CRUD------------------------------------------------------------

    @Transactional
    public void save(@NonNull final Long questionId, final byte star) {
        Long userId = securityUtils.getAuthUserId();
        UserQuestionStarredId id = new UserQuestionStarredId(userId, questionId);
        UserQuestionStarred userQuestionStarred = new UserQuestionStarred();
        userQuestionStarred.setUserQuestionStarredId(id);
        userQuestionStarred.setUser(em.getReference(User.class, userId));
        userQuestionStarred.setQuestion(em.getReference(Question.class, questionId));
        userQuestionStarred.setStar(star);
        userQuestionStarred.setWhenStarred(LocalDateTime.now());
        userQuestionStarredRepository.save(userQuestionStarred);
    }

    @Transactional
    public void deleteById(@NonNull final Long questionId) {
        Long userId = securityUtils.getAuthUserId();
        userQuestionStarredRepository.deleteById(new UserQuestionStarredId(userId, questionId));
    }

    //--------------------------------------------One to see with answers-----------------------------------------------

    @Transactional(readOnly = true)
    // Full question with answers, no correct answer
    public QuestionSessionOutDto findOneByQuestionId(@NonNull final Long questionId) {
        Long userId = securityUtils.getAuthUserId();
        UserQuestionStarred userQuestionStarred = userQuestionStarredRepository.findById(new UserQuestionStarredId(userId, questionId))
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND));
        return userQuestionStarredTransformer.toDtoExt(userQuestionStarred);
    }

    //------------------------------------------------User short table--------------------------------------------------

    @Transactional(readOnly = true)
    // Only questions without answers
    public Page<QuestionSessionMinOutDto> findAllByUserId(@NonNull final Pageable pageable) {
        return userQuestionStarredRepository.findAllByUserId(securityUtils.getAuthUserId(), pageable).map(userQuestionStarredTransformer::toDto);
    }

    // To check if limit is overflowed
    @Transactional(readOnly = true)
    public long countByUserId() {
        return userQuestionStarredRepository.countByUserId(securityUtils.getAuthUserId());
    }
}

package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.question.*;
import ua.edu.ratos.dao.repository.QuestionRepository;
import ua.edu.ratos.service.dto.in.*;
import ua.edu.ratos.service.dto.out.question.*;
import ua.edu.ratos.service.transformer.mapper.QuestionMapper;
import ua.edu.ratos.service.transformer.QuestionTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionService {

    private static final String QUESTION_NOT_FOUND = "The requested Question is not found, questionId = ";

    @PersistenceContext
    private final EntityManager em;

    private final QuestionRepository questionRepository;

    private final QuestionTransformer questionTransformer;

    private final QuestionMapper questionMapper;

    //---------------------------------------------------CRUD-----------------------------------------------------------
    @Transactional
    public QuestionMCQOutDto save(@NonNull final QuestionMCQInDto dto) {
        QuestionMCQ questionMCQ = questionRepository.save(questionTransformer.toEntity(dto));
        return questionMapper.toDto(questionMCQ);
    }

    @Transactional
    public QuestionMCQOutDto update(@NonNull final QuestionMCQInDto dto) {
        if (dto.getQuestionId()==null)
            throw new RuntimeException("Failed to update question MCQ, questionId is not present!");
        QuestionMCQ questionMCQ = questionRepository.save(questionTransformer.toEntity(dto));
        return questionMapper.toDto(questionMCQ);
    }

    @Transactional
    public QuestionFBSQOutDto save(@NonNull final QuestionFBSQInDto dto) {
        QuestionFBSQ questionFBSQ = questionRepository.save(questionTransformer.toEntity(dto));
        return questionMapper.toDto(questionFBSQ);
    }

    @Transactional
    public QuestionFBMQOutDto save(@NonNull final QuestionFBMQInDto dto) {
        QuestionFBMQ questionFBMQ = questionRepository.save(questionTransformer.toEntity(dto));
        return questionMapper.toDto(questionFBMQ);
    }

    @Transactional
    public QuestionMQOutDto save(@NonNull final QuestionMQInDto dto) {
        QuestionMQ questionMQ = questionRepository.save(questionTransformer.toEntity(dto));
        return questionMapper.toDto(questionMQ);
    }

    @Transactional
    public QuestionSQOutDto save(@NonNull final QuestionSQInDto dto) {
        QuestionSQ questionSQ = questionRepository.save(questionTransformer.toEntity(dto));
        return questionMapper.toDto(questionSQ);
    }

    /**
     * Questions obtained after parsing the .rtp- .txt-files
     * @param questions batch of questions from a file
     */
    @Transactional
    public void saveAll(@NonNull final List<Question> questions) {
        questionRepository.saveAll(questions);
    }

    @Transactional
    public void updateName(@NonNull final Long questionId, @NonNull final String name) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.setQuestion(name);
    }

    @Transactional
    public void updateLevel(@NonNull final Long questionId, @NonNull final Byte level) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.setLevel(level);
    }

    @Transactional
    public void updateRequired(@NonNull final Long questionId, @NonNull final Boolean required) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.setRequired(required);
    }

    @Transactional
    public void associateWithHelp(@NonNull final Long questionId, @NonNull final Long helpId) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.addHelp(em.getReference(Help.class, helpId));
    }

    @Transactional
    public void disassociateWithHelp(@NonNull final Long questionId) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.clearHelps();
    }

    @Transactional
    public void associateWithResource(@NonNull final Long questionId, @NonNull final Long resourceId) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.addResource(em.getReference(Resource.class, resourceId));
    }

    @Transactional
    public void disassociateWithResource(@NonNull final Long questionId) {
        final Question entity = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId));
        entity.clearResources();
    }

    @Transactional
    public void deleteById(@NonNull final Long questionId) {
        questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException(QUESTION_NOT_FOUND + questionId))
                .setDeleted(true);
    }

    //-------------------------------------------------------Session----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<Question> findAllForSessionByThemeIdAndType(@NonNull final Long themeId, @NonNull final Long typeId, byte level) {
        return questionRepository.findAllForSimpleSessionByThemeTypeAndLevel(themeId, typeId, level);
    }

    //----------------------------------------------------Cached session------------------------------------------------
    @Cacheable(value = "question", key = "{'MCQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionMCQ> findAllMCQForCachedSessionByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllMCQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @Cacheable(value = "question", key = "{'FBSQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionFBSQ> findAllFBSQForCachedSessionByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllFBSQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @Cacheable(value = "question", key = "{'FBMQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionFBMQ> findAllFBMQForCachedSessionByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllFBMQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @Cacheable(value = "question", key = "{'MQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionMQ> findAllMQForCachedSessionByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllMQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @Cacheable(value = "question", key = "{'SQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionSQ> findAllSQForCachedSessionByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllSQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    // -----------------------------------------------Update Cache------------------------------------------------------
    @CachePut(value = "question", key = "{'MCQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionMCQ> findAllMCQForCacheUpdateByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllMCQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @CachePut(value = "question", key = "{'FBSQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionFBSQ> findAllFBSQForCacheUpdateByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllFBSQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @CachePut(value = "question", key = "{'FBMQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionFBMQ> findAllFBMQForCacheUpdateByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllFBMQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @CachePut(value = "question", key = "{'MQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionMQ> findAllMQForCacheUpdateByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllMQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    @CachePut(value = "question", key = "{'SQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public Set<QuestionSQ> findAllSQForCacheUpdateByThemeId(@NonNull final Long themeId, byte level) {
        return questionRepository.findAllSQForCachedSessionWithEverythingByThemeAndLevel(themeId, level);
    }

    //-------------------------------------------------Invalidate cache-------------------------------------------------
    // Consider use @CachePut methods instead!
    @CacheEvict(value = "question", key = "{'MCQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public void invalidateCacheOfAllMCQByThemeId(@NonNull final Long themeId, byte level) {
    }

    @CacheEvict(value = "question", key = "{'FBSQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public void invalidateCacheOfAllFBSQByThemeId(@NonNull final Long themeId, byte level) {
    }

    @CacheEvict(value = "question", key = "{'FBMQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public void invalidateCacheOfAllFBMQByThemeId(@NonNull final Long themeId, byte level) {
    }

    @CacheEvict(value = "question", key = "{'MQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public void invalidateCacheOfAllMQByThemeId(@NonNull final Long themeId, byte level) {
    }

    @CacheEvict(value = "question", key = "{'SQ', #themeId, #level}")
    @Transactional(readOnly = true)
    public void invalidateCacheOfAllSQByThemeId(@NonNull final Long themeId, byte level) {
    }

    //----------------------------------------------------Staff table---------------------------------------------------

    @Transactional(readOnly = true)
    public Set<QuestionMCQOutDto> findAllMCQForEditByThemeId(@NonNull final Long themeId) {
        return questionRepository
                .findAllMCQForEditByThemeId(themeId)
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toSet());
    }
}

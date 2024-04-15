package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.QuestionRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.out.question.*;
import ua.edu.ratos.service.transformer.mapper.QuestionMapper;

import jakarta.persistence.EntityNotFoundException;

/**
 * For future references, when the quantity if questions per theme will be measured in 1000-10000;
 * For simplistic case, use:
 * @see QuestionService
 */
@Service
@AllArgsConstructor
public class PagedQuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final SecurityUtils securityUtils;


    //----------------------------------------------------One for edit--------------------------------------------------
    @Transactional(readOnly = true)
    public QuestionMCQOutDto findOneMCQForEditById(@NonNull final Long questionId) {
        return questionMapper.toDto(questionRepository.findOneMCQForEditById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("QuestionMCQ is not found , questionId = " + questionId)));
    }

    @Transactional(readOnly = true)
    public QuestionFBSQOutDto findOneFBSQForEditById(@NonNull final Long questionId) {
        return questionMapper.toDto(questionRepository.findOneFBSQForEditById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("QuestionFBSQ is not found, questionId = " + questionId)));
    }

    @Transactional(readOnly = true)
    public QuestionFBMQOutDto findOneFBMQForEditById(@NonNull final Long questionId) {
        return questionMapper.toDto(questionRepository.findOneFBMQForEditById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("QuestionFBMQ is not found, questionId = " + questionId)));
    }

    @Transactional(readOnly = true)
    public QuestionMQOutDto findOneMQForEditById(@NonNull final Long questionId) {
        return questionMapper.toDto(questionRepository.findOneMQForEditById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("QuestionMQ is not found, questionId = " + questionId)));
    }

    @Transactional(readOnly = true)
    public QuestionSQOutDto findOneSQForEditById(@NonNull final Long questionId) {
        return questionMapper.toDto(questionRepository.findOneSQForEditById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("QuestionSQ is not found, questionId = " + questionId)));
    }

    //----------------------------------------------------Staff table---------------------------------------------------

    @Transactional(readOnly = true)
    public Page<QuestionMCQOutDto> findAllMCQForEditByThemeId(@NonNull final Long themeId, @NonNull final Pageable pageable) {
        return questionRepository.findAllMCQForEditByThemeId(themeId, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<QuestionFBSQOutDto> findAllFBSQForEditByThemeId(@NonNull final Long themeId, @NonNull final Pageable pageable) {
        return questionRepository.findAllFBSQForEditByThemeId(themeId, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<QuestionFBMQOutDto> findAllFBMQForEditByThemeId(@NonNull final Long themeId, @NonNull final Pageable pageable) {
        return questionRepository.findAllFBMQForEditByThemeId(themeId, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<QuestionMQOutDto> findAllMQForEditByThemeId(@NonNull final Long themeId, @NonNull final Pageable pageable) {
        return questionRepository.findAllMQForEditByThemeId(themeId, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<QuestionSQOutDto> findAllSQForEditByThemeId(@NonNull final Long themeId, @NonNull final Pageable pageable) {
        return questionRepository.findAllSQForEditByThemeId(themeId, pageable).map(questionMapper::toDto);
    }

    //---------------------------------------Staff (global search throughout department)--------------------------------
    @Transactional(readOnly = true)
    public Slice<QuestionMCQOutDto> findAllMCQForSearchByDepartmentIdAndTitleContains(@NonNull final String starts, @NonNull final Pageable pageable) {
        Long depId = securityUtils.getAuthDepId();
        return questionRepository.findAllMCQForSearchByDepartmentIdAndTitleContains(depId, starts, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<QuestionFBSQOutDto> findAllFBSQForSearchByDepartmentIdAndTitleContains(@NonNull final String starts, @NonNull final Pageable pageable) {
        Long depId = securityUtils.getAuthDepId();
        return questionRepository.findAllFBSQForSearchByDepartmentIdAndTitleContains(depId, starts, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<QuestionFBMQOutDto> findAllFBMQForSearchByDepartmentIdAndTitleContains(@NonNull final String starts, @NonNull final Pageable pageable) {
        Long depId = securityUtils.getAuthDepId();
        return questionRepository.findAllFBMQForSearchByDepartmentIdAndTitleContains(depId, starts, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<QuestionMQOutDto> findAllMQForSearchByDepartmentIdAndTitleContains(@NonNull final String starts, @NonNull final Pageable pageable) {
        Long depId = securityUtils.getAuthDepId();
        return questionRepository.findAllMQForSearchByDepartmentIdAndTitleContains(depId, starts, pageable).map(questionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<QuestionSQOutDto> findAllSQForSearchByDepartmentIdAndTitleContains(@NonNull final String starts, @NonNull final Pageable pageable) {
        Long depId = securityUtils.getAuthDepId();
        return questionRepository.findAllSQForSearchByDepartmentIdAndTitleContains(depId, starts, pageable).map(questionMapper::toDto);
    }
}

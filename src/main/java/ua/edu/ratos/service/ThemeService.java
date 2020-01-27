package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Theme;
import ua.edu.ratos.dao.entity.question.Question;
import ua.edu.ratos.dao.repository.QuestionRepository;
import ua.edu.ratos.dao.repository.ThemeRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ThemeInDto;
import ua.edu.ratos.service.dto.out.ThemeExtOutDto;
import ua.edu.ratos.service.dto.out.ThemeMapOutDto;
import ua.edu.ratos.service.dto.out.ThemeMinOutDto;
import ua.edu.ratos.service.dto.out.ThemeOutDto;
import ua.edu.ratos.service.transformer.dto_to_entity.DtoThemeTransformer;
import ua.edu.ratos.service.transformer.entity_to_dto.ThemeDtoTransformer;
import ua.edu.ratos.service.transformer.entity_to_dto.ThemeExtDtoTransformer;
import ua.edu.ratos.service.transformer.entity_to_dto.ThemeMapDtoTransformer;
import ua.edu.ratos.service.transformer.entity_to_dto.ThemeMinDtoTransformer;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ThemeService {

    private static final String THEME_NOT_FOUND = "Requested theme is not found, themeId = ";

    private final ThemeRepository themeRepository;

    private final QuestionRepository questionRepository;

    private final DtoThemeTransformer dtoThemeTransformer;

    private final ThemeDtoTransformer themeDtoTransformer;

    private final ThemeMinDtoTransformer themeMinDtoTransformer;

    private final ThemeExtDtoTransformer themeExtDtoTransformer;

    private final ThemeMapDtoTransformer themeMapDtoTransformer;

    private final AccessChecker accessChecker;

    private final SecurityUtils securityUtils;

    //---------------------------------------------------CRUD-----------------------------------------------------------
    @Transactional
    public ThemeOutDto save(@NonNull final ThemeInDto dto) {
        Theme theme = dtoThemeTransformer.toEntity(dto);
        theme = themeRepository.save(theme);
        return themeDtoTransformer.toDto(theme);
    }

    @Transactional
    public ThemeOutDto update(@NonNull final ThemeInDto dto) {
        if (dto.getThemeId()==null)
            throw new RuntimeException("Failed to update, themeId is nullable!");
        Theme theme = checkModificationPossibility(dto.getThemeId());
        theme = dtoThemeTransformer.toEntity(theme, dto);
        Theme result = themeRepository.save(theme);
        return themeDtoTransformer.toDto(result);
    }

    @Transactional
    public void updateName(@NonNull final Long themeId, @NonNull final String name) {
        Theme theme = checkModificationPossibility(themeId);
        theme.setName(name);
    }

    @Transactional
    public void deleteById(@NonNull final Long themeId) {
        Theme theme = checkModificationPossibility(themeId);
        themeRepository.delete(theme);
    }

    @Transactional
    public void deleteByIdSoft(@NonNull final Long themeId) {
        Theme theme = checkModificationPossibility(themeId);
        theme.setDeleted(true);
    }

    private Theme checkModificationPossibility(@NonNull final Long themeId) {
        Theme theme = themeRepository.findForSecurityById(themeId)
                .orElseThrow(()->new EntityNotFoundException(THEME_NOT_FOUND+themeId));
        accessChecker.checkModifyAccess(theme.getAccess(), theme.getStaff());
        return theme;
    }

    //-------------------------------------------------Staff (min for drop down)----------------------------------------
    @Transactional(readOnly = true)
    public Set<ThemeMinOutDto> findAllForDropdownByStaffId() {
        return themeRepository.findAllForDropDownByStaffId(securityUtils.getAuthStaffId())
                .stream()
                .map(themeMinDtoTransformer::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<ThemeMinOutDto> findAllForDropdownByDepartmentId() {
        return themeRepository.findAllForDropDownByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(themeMinDtoTransformer::toDto)
                .collect(Collectors.toSet());
    }


    @Transactional(readOnly = true)
    public Set<ThemeMinOutDto> findAllForDropdownByDepartmentId(@NonNull final Long depId) {
        return themeRepository.findAllForDropDownByDepartmentId(depId)
                .stream()
                .map(themeMinDtoTransformer::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff (table)--------------------------------------------------
    @Transactional(readOnly = true)
    public Set<ThemeOutDto> findAllForTableByDepartment() {
        return themeRepository.findAllForTableByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(themeDtoTransformer::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<ThemeOutDto> findAllForTableByDepartmentId(@NonNull Long depId) {
        return themeRepository.findAllForTableByDepartmentId(depId)
                .stream()
                .map(themeDtoTransformer::toDto)
                .collect(Collectors.toSet());
    }

    //----------------------------------------------Scheme creating support---------------------------------------------
    @Transactional(readOnly = true)
    public ThemeMapOutDto getQuestionTypeLevelMapByThemeId(@NonNull final Long themeId) {
        Set<Question> questions = questionRepository.findAllForTypeLevelMapByThemeId(themeId);
        if (questions==null || questions.isEmpty()) throw new IllegalStateException("No questions in this theme!");
        return themeMapDtoTransformer.toDto(themeId, questions);
    }

    //-------------------Experimental (+details on how many questions of which type the theme contains)-----------------
    @Transactional(readOnly = true)
    public ThemeExtOutDto findByIdForQuestionsDetails(@NonNull final Long themeId) {
        return themeExtDtoTransformer.toDto(themeRepository.findById(themeId)
                .orElseThrow(()->new EntityNotFoundException(THEME_NOT_FOUND+themeId)));
    }

    // -----------------------------------------------Admin table-------------------------------------------------------
    @Transactional(readOnly = true)
    public Page<ThemeOutDto> findAll(@NonNull final Pageable pageable) {
        return themeRepository.findAll(pageable).map(themeDtoTransformer::toDto);
    }

}

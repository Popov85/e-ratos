package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.repository.SchemeRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.SchemeInDto;
import ua.edu.ratos.service.dto.out.SchemeInfoOutDto;
import ua.edu.ratos.service.dto.out.SchemeMinOutDto;
import ua.edu.ratos.service.dto.out.SchemeOutDto;
import ua.edu.ratos.service.dto.out.SchemeShortOutDto;
import ua.edu.ratos.service.grading.SchemeGradingManagerService;
import ua.edu.ratos.service.transformer.*;
import ua.edu.ratos.service.transformer.mapper.SchemeInfoMapper;
import ua.edu.ratos.service.transformer.mapper.SchemeMapper;
import ua.edu.ratos.service.transformer.mapper.SchemeMinMapper;
import ua.edu.ratos.service.transformer.mapper.SchemeShortMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SchemeService {

    private static final String SCHEME_NOT_FOUND_ID = "Requested scheme not found, schemeId = ";

    private final SchemeRepository schemeRepository;

    private final SchemeTransformer schemeTransformer;

    private final SchemeGradingManagerService gradingManagerService;

    private final SchemeShortMapper schemeShortMapper;

    private final SchemeMapper schemeMapper;

    private final SchemeMinMapper schemeMinMapper;

    private final SchemeInfoMapper schemeInfoMapper;

    private final AccessChecker accessChecker;

    private final SecurityUtils securityUtils;


    //---------------------------------------------------CRUD-----------------------------------------------------------
    @Transactional
    public SchemeOutDto save(@NonNull final SchemeInDto dto) {
        Scheme scheme = schemeTransformer.toEntity(dto);
        scheme = schemeRepository.save(scheme);
        // By design, we cannot cascade saving of the grading details, we do it separately in the same transaction,
        // depending on the scheme's grading questionType specified and ID of selected details
        final long gradingId = dto.getGradingId();
        final long gradingDetailsId = dto.getGradingDetailsId();
        gradingManagerService.save(scheme.getSchemeId(), gradingId, gradingDetailsId);
        return schemeMapper.toDto(scheme);
    }

    @Transactional
    public SchemeOutDto update(@NonNull final SchemeInDto dto) {
        if (dto.getSchemeId()==null)
            throw new RuntimeException("Failed to update Scheme, schemeId is not present!");
        checkModificationPossibility(dto.getSchemeId());
        return save(dto);
    }

    @Transactional
    public void updateName(@NonNull final Long schemeId, @NonNull final String name) {
        Scheme scheme = checkModificationPossibility(schemeId);
        scheme.setName(name);
    }

    @Transactional
    public void updateIsActive(@NonNull final Long schemeId, boolean isActive) {
        Scheme scheme = checkModificationPossibility(schemeId);
        scheme.setActive(isActive);
    }

    @Transactional
    public void updateIsLmsOnly(@NonNull final Long schemeId, boolean isLmsOnly) {
        Scheme scheme = checkModificationPossibility(schemeId);
        scheme.setLmsOnly(isLmsOnly);
    }

    @Transactional
    public void deleteById(@NonNull final Long schemeId) {
        Scheme scheme = checkModificationPossibility(schemeId);
        scheme.setDeleted(true);
    }

    private Scheme checkModificationPossibility(@NonNull final Long schemeId) {
        Scheme scheme = schemeRepository.findForSecurityById(schemeId)
                .orElseThrow(() -> new EntityNotFoundException(SCHEME_NOT_FOUND_ID + schemeId));
        accessChecker.checkModifyAccess(scheme.getAccess(), scheme.getStaff());
        return scheme;
    }

    //------------------------------------------------One (for edit)----------------------------------------------------
    @Transactional(readOnly = true)
    public SchemeOutDto findByIdForEdit(@NonNull final Long schemeId) {
        return schemeMapper.toDto(schemeRepository.findForEditById(schemeId)
                .orElseThrow(() -> new EntityNotFoundException(SCHEME_NOT_FOUND_ID + schemeId)));
    }

    //--------------------------------------------One (for cached session)----------------------------------------------
    @Transactional(readOnly = true)
    public Scheme findByIdForSession(@NonNull final Long schemeId) {
        return schemeRepository.findForSessionById(schemeId)
                .orElseThrow(() -> new EntityNotFoundException(SCHEME_NOT_FOUND_ID + schemeId));
    }

    //-------------------------------------------One for getting basic Info---------------------------------------------
    @Transactional(readOnly = true)
    public SchemeInfoOutDto findByIdForInfo(@NonNull final Long schemeId) {
        return schemeInfoMapper.toDto(schemeRepository.findForInfoById(schemeId)
                .orElseThrow(() -> new EntityNotFoundException(SCHEME_NOT_FOUND_ID + schemeId)));
    }

    //-------------------------------------------------Staff tables-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<SchemeShortOutDto> findAllByDepartment() {
        return schemeRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(schemeShortMapper::toDto)
                .collect(Collectors.toSet());
    }

    //------------------------------------------------Drop-down (results)-----------------------------------------------
    @Transactional(readOnly = true)
    public Set<SchemeMinOutDto> findAllForDropdownByCourseId(@NonNull final Long courseId) {
        return schemeRepository.findAllForDropDownByCourseId(courseId)
                .stream()
                .map(schemeMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<SchemeMinOutDto> findAllForDropdownByDepartmentId(@NonNull final Long depId) {
        return schemeRepository.findAllForDropDownByDepartmentId(depId)
                .stream()
                .map(schemeMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<SchemeMinOutDto> findAllForDropdownByDepartmentId() {
        return schemeRepository.findAllForDropDownByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(schemeMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    //----------------------------------------------Start-up cache update-----------------------------------------------
    @Transactional(readOnly = true)
    public Slice<Scheme> findAllForCachedSession(@NonNull final Pageable pageable) {
        return schemeRepository.findAllForCachedSession(pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Scheme> findLargeForCachedSession(@NonNull final Pageable pageable) {
        return schemeRepository.findLargeForCachedSession(pageable);
    }

    //-----------------------------------------------Runtime cache update-----------------------------------------------
    @Transactional(readOnly = true)
    public Slice<Scheme> findCoursesSchemesForCachedSession(@NonNull final Pageable pageable, @NonNull final Long courseId) {
        return schemeRepository.findCoursesSchemesForCachedSession(courseId, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Scheme> findDepartmentSchemesForCachedSession(@NonNull final Pageable pageable, @NonNull final Long depId) {
        return schemeRepository.findDepartmentSchemesForCachedSession(depId, pageable);
    }

    //-------------------------------------------------Admin (for table)------------------------------------------------
    @Transactional(readOnly = true)
    public Page<SchemeShortOutDto> findAll(@NonNull final Pageable pageable) {
        return schemeRepository.findAll(pageable).map(schemeShortMapper::toDto);
    }
}

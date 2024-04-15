package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.lms.LMS;
import ua.edu.ratos.dao.repository.lms.LMSRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.LMSInDto;
import ua.edu.ratos.service.dto.out.LMSMinOutDto;
import ua.edu.ratos.service.dto.out.LMSOutDto;
import ua.edu.ratos.service.transformer.mapper.LMSMapper;
import ua.edu.ratos.service.transformer.mapper.LMSMinMapper;
import ua.edu.ratos.service.transformer.LMSTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class LMSService {

    private static final String LMS_NOT_FOUND = "The requested LMS is not found, lmsId = ";

    private final LMSRepository lmsRepository;

    private final LMSTransformer LMSTransformer;

    private final LMSMapper lmsMapper;

    private final LMSMinMapper lmsMinMapper;

    private final SecurityUtils securityUtils;

    //----------------------------------------------------CRUD----------------------------------------------------------
    @Transactional
    public LMSOutDto save(@NonNull final LMSInDto dto) {
        LMS lms = lmsRepository.save(LMSTransformer.toEntity(dto));
        return lmsMapper.toDto(lms);
    }

    @Transactional
    public LMSOutDto update (@NonNull final LMSInDto dto) {
        Long lmsId = dto.getLmsId();
        if (lmsId ==null)
            throw new RuntimeException("Failed to update, lmsId is nullable");
        LMS lms = lmsRepository.findById(lmsId)
                .orElseThrow(()->new EntityNotFoundException(LMS_NOT_FOUND+lmsId));
        lms = LMSTransformer.toEntity(lms, dto);
        LMSOutDto lmsOutDto = lmsMapper.toDto(lms);
        return lmsOutDto;
    }

    @Transactional
    public void updateName(@NonNull final Long lmsId, @NonNull final String name) {
        lmsRepository.findById(lmsId)
                .orElseThrow(() -> new EntityNotFoundException(LMS_NOT_FOUND + lmsId))
                .setName(name);
    }

    @Transactional
    public void deleteById(@NonNull final Long lmsId) {
        lmsRepository.deleteById(lmsId);
    }

    @Transactional
    public void deleteByIdSoft(@NonNull final Long lmsId) {
        lmsRepository.findById(lmsId)
                .orElseThrow(() -> new EntityNotFoundException(LMS_NOT_FOUND + lmsId))
                .setDeleted(true);
    }

    //-----------------------------------------------------Staff drop-down----------------------------------------------

    @Transactional(readOnly = true)
    public Set<LMSMinOutDto> findAllForDropdownByOrganisation() {
        return lmsRepository.findAllForDropdownByOrgId(securityUtils.getAuthOrgId())
                .stream()
                .map(lmsMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------------ORG admin table----------------------------------------------

    @Transactional(readOnly = true)
    public Set<LMSOutDto> findAllForTableByOrganisation() {
        return lmsRepository.findAllForTableByOrgId(securityUtils.getAuthOrgId())
                .stream()
                .map(lmsMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<LMSOutDto> findAllForTableByOrganisationId(@NonNull final Long orgId) {
        return lmsRepository.findAllForDropdownByOrgId(orgId)
                .stream()
                .map(lmsMapper::toDto)
                .collect(Collectors.toSet());
    }

    //--------------------------------------------------------ADMIN table-----------------------------------------------
    @Transactional(readOnly = true)
    public Page<LMSOutDto> findAllAdmin(@NonNull final Pageable pageable) {
        return lmsRepository.findAllAdmin(pageable).map(lmsMapper::toDto);
    }
}

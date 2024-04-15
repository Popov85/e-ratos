package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.repository.HelpRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.HelpInDto;
import ua.edu.ratos.service.dto.out.HelpOutDto;
import ua.edu.ratos.service.transformer.mapper.HelpMapper;
import ua.edu.ratos.service.transformer.HelpTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class HelpService {

    private static final String HELP_NOT_FOUND = "The requested Help not found, helpId = ";

    @PersistenceContext
    private final EntityManager em;

    private final HelpRepository helpRepository;

    private final HelpTransformer helpTransformer;

    private final HelpMapper helpMapper;

    private final SecurityUtils securityUtils;


    //-------------------------------------------------------CRUD-------------------------------------------------------
    @Transactional
    public HelpOutDto save(@NonNull final HelpInDto dto) {
        Help help = helpTransformer.toEntity(dto);
        return helpMapper.toDto(helpRepository.save(help));
    }

    @Transactional
    public HelpOutDto update(@NonNull final HelpInDto dto) {
        if (dto.getHelpId()==null)
            throw new RuntimeException("Failed to update a help, nullable helpId");
        Help help = helpTransformer.toEntity(dto);
        return helpMapper.toDto(helpRepository.save(help));
    }

    @Transactional
    public void updateName(@NonNull final Long helpId, @NonNull final String name) {
        helpRepository.findById(helpId)
                .orElseThrow(() -> new EntityNotFoundException(HELP_NOT_FOUND + helpId))
                .setName(name);
    }

    @Transactional
    public void updateHelp(@NonNull final Long helpId, @NonNull final String help) {
        helpRepository.findById(helpId)
                .orElseThrow(() -> new EntityNotFoundException(HELP_NOT_FOUND + helpId))
                .setHelp(help);
    }

    @Transactional
    public void updateResource(@NonNull final Long helpId, @NonNull final Long resId) {
        Help help = helpRepository.findById(helpId)
                .orElseThrow(() -> new EntityNotFoundException(HELP_NOT_FOUND + helpId));
        help.clearResources();
        help.addResource(em.getReference(Resource.class, resId));
    }

    @Transactional
    public void deleteByIdSoft(@NonNull final Long helpId) {
        helpRepository.findById(helpId)
                .orElseThrow(() -> new EntityNotFoundException(HELP_NOT_FOUND + helpId))
                .setDeleted(true);
    }

    @Transactional
    public void deleteById(@NonNull final Long helpId) {
        helpRepository.deleteById(helpId);
    }


    //----------------------------------------------------Staff table---------------------------------------------------
    @Transactional(readOnly = true)
    public Set<HelpOutDto> findAllByDepartment() {
        log.debug("Service got job, depId = {}", securityUtils.getAuthDepId());
        return helpRepository.findAllByDepartment(securityUtils.getAuthDepId())
                .stream()
                .map(helpMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------Staff table for future references----------------------------------------
    @Transactional(readOnly = true)
    public Page<HelpOutDto> findAllByStaffId(@NonNull final Pageable pageable) {
        return helpRepository.findAllByStaffId(securityUtils.getAuthStaffId(), pageable).map(helpMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<HelpOutDto> findAllByStaffIdAndNameLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return helpRepository.findAllByStaffIdAndNameLettersContains(securityUtils.getAuthStaffId(), letters, pageable).map(helpMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<HelpOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        return helpRepository.findAllByDepartmentId(securityUtils.getAuthDepId(), pageable).map(helpMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<HelpOutDto> findAllByDepartmentIdAndNameLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return helpRepository.findAllByDepartmentIdAndNameLettersContains(securityUtils.getAuthDepId(), letters, pageable).map(helpMapper::toDto);
    }

    //----------------------------------------------------ADMIN table---------------------------------------------------
    @Transactional(readOnly = true)
    public Page<HelpOutDto> findAll(@NonNull final Pageable pageable) {
        return helpRepository.findAll(pageable).map(helpMapper::toDto);
    }

}

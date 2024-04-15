package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Options;
import ua.edu.ratos.dao.repository.OptionsRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.OptionsInDto;
import ua.edu.ratos.service.dto.out.OptionsOutDto;
import ua.edu.ratos.service.transformer.mapper.OptionsMapper;
import ua.edu.ratos.service.transformer.OptionsTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OptionsService {

    private static final String ID_IS_NOT_INCLUDED = "OptId is not included, reject update!";
    private static final String OPTIONS_NOT_FOUND = "The requested Options is not found, optId = ";
    private static final String DEFAULT_OPTIONS_CANNOT_BE_MODIFIED = "Default options cannot be modified!";

    private final OptionsRepository optionsRepository;

    private final OptionsTransformer optionsTransformer;

    private final OptionsMapper optionsMapper;

    private final SecurityUtils securityUtils;

    //-----------------------------------------------------CRUD---------------------------------------------------------
    @Transactional
    public OptionsOutDto save(@NonNull final OptionsInDto dto) {
        Options options = optionsRepository.save(optionsTransformer.toEntity(dto));
        return optionsMapper.toDto(options);
    }

    // Not the "redundant save" anti-pattern!!
    // Exactly 2 queries: select, update + dynamic update(!)
    @Transactional
    public OptionsOutDto update(@NonNull final OptionsInDto dto) {
        Long optId = dto.getOptId();
        if (optId == null || optId == 0)
            throw new RuntimeException(ID_IS_NOT_INCLUDED + optId);
        Options options = optionsRepository.findById(optId)
                .orElseThrow(() -> new EntityNotFoundException(OPTIONS_NOT_FOUND + optId));
        if (options.isDefault())
            throw new RuntimeException(DEFAULT_OPTIONS_CANNOT_BE_MODIFIED );
        // Will merge actually
        Options updOptions = optionsRepository.save(optionsTransformer.toEntity(dto));
        return optionsMapper.toDto(updOptions);
    }

    @Transactional
    public void deleteById(@NonNull final Long optId) {
        Options options = optionsRepository.findById(optId)
                .orElseThrow(() -> new EntityNotFoundException(OPTIONS_NOT_FOUND  + optId));
        if (options.isDefault()) throw new RuntimeException(DEFAULT_OPTIONS_CANNOT_BE_MODIFIED);
        options.setDeleted(true);
    }

    //-------------------------------------------------One (for update)-------------------------------------------------
    @Transactional(readOnly = true)
    public OptionsOutDto findOneForEdit(@NonNull final Long optId) {
        return optionsMapper.toDto(optionsRepository.findOneForEdit(optId)
                .orElseThrow(()->new EntityNotFoundException(OPTIONS_NOT_FOUND +optId)));
    }

    //------------------------------------------------------Default-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<OptionsOutDto> findAllDefault() {
        return optionsRepository.findAllDefault()
                .stream()
                .map(optionsMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff table/drop-down------------------------------------------
    @Transactional(readOnly = true)
    public Set<OptionsOutDto> findAllByDepartment() {
        return optionsRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(optionsMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Staff table/drop-down+default--------------------------------------

    @Transactional(readOnly = true)
    public Set<OptionsOutDto> findAllByDepartmentWithDefault() {
        Set<OptionsOutDto> result = findAllByDepartment();
        result.addAll(findAllDefault());
        return result;
    }

    //--------------------------------------------------------ADMIN-----------------------------------------------------
    @Transactional(readOnly = true)
    public Page<OptionsOutDto> findAllAdmin(@NonNull Pageable pageable) {
        return optionsRepository.findAllAdmin(pageable).map(optionsMapper::toDto);
    }

}

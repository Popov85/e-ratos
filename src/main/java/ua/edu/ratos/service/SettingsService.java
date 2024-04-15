package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Settings;
import ua.edu.ratos.dao.repository.SettingsRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.SettingsInDto;
import ua.edu.ratos.service.dto.out.SettingsOutDto;
import ua.edu.ratos.service.transformer.mapper.SettingsMapper;
import ua.edu.ratos.service.transformer.SettingsTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SettingsService {

    private static final String ID_IS_NOT_INCLUDED = "SetId is not included, reject update!";
    private static final String DEFAULT_SETTINGS_CANNOT_BE_MODIFIED = "Default settings cannot be modified!";

    private final SettingsRepository settingsRepository;

    private final SettingsTransformer settingsTransformer;

    private final SettingsMapper settingsMapper;

    private final SecurityUtils securityUtils;

    //-----------------------------------------------------CRUD---------------------------------------------------------
    @Transactional
    public SettingsOutDto save(@NonNull final SettingsInDto dto) {
        Settings settings = settingsRepository.save(settingsTransformer.toEntity(dto));
        return settingsMapper.toDto(settings);
    }

    // Not the "redundant save" anti-pattern!!
    // Exactly 2 queries: select, update + dynamic update(!)
    @Transactional
    public SettingsOutDto update(@NonNull final SettingsInDto dto) {
        Long setId = dto.getSetId();
        if (setId == null || setId == 0)
            throw new RuntimeException(ID_IS_NOT_INCLUDED + setId);
        Settings settings = settingsRepository.findById(setId)
                .orElseThrow(() -> new EntityNotFoundException("Settings not found"+ setId));
        if (settings.isDefault())
            throw new RuntimeException(DEFAULT_SETTINGS_CANNOT_BE_MODIFIED );
        // Will merge actually
        Settings updSave = settingsRepository.save(settingsTransformer.toEntity(dto));
        return settingsMapper.toDto(updSave);
    }

    @Transactional
    public void deleteById(@NonNull final Long setId) {
        Settings settings = settingsRepository.findById(setId)
                .orElseThrow(() -> new EntityNotFoundException("Settings not found" + setId));
        if (settings.isDefault()) throw new RuntimeException(DEFAULT_SETTINGS_CANNOT_BE_MODIFIED);
        settings.setDeleted(true);
    }

    //-------------------------------------------------One (for update)-------------------------------------------------
    @Transactional(readOnly = true)
    public SettingsOutDto findOneForEdit(@NonNull final Long setId) {
        return settingsMapper.toDto(settingsRepository.findOneForEdit(setId)
                .orElseThrow(()->new EntityNotFoundException("Settings is not found, setId = "+setId)));
    }

    //------------------------------------------------------Default-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<SettingsOutDto> findAllDefault() {
        return settingsRepository.findAllDefault()
                .stream()
                .map(settingsMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff table/drop-down------------------------------------------
    @Transactional(readOnly = true)
    public Set<SettingsOutDto> findAllByDepartment() {
        return settingsRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(settingsMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Staff table/drop-down+default--------------------------------------

    @Transactional(readOnly = true)
    public Set<SettingsOutDto> findAllByDepartmentWithDefault() {
        Set<SettingsOutDto> result = findAllByDepartment();
        result.addAll(findAllDefault());
        return result;
    }

    //--------------------------------------------------------ADMIN-----------------------------------------------------
    @Transactional(readOnly = true)
    public Page<SettingsOutDto> findAll(@NonNull Pageable pageable) {
        return settingsRepository.findAll(pageable).map(settingsMapper::toDto);
    }

}

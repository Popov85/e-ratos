package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.SettingsFBRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.SettingsFBInDto;
import ua.edu.ratos.service.dto.out.SettingsFBOutDto;
import ua.edu.ratos.service.transformer.mapper.SettingsFBMapper;
import ua.edu.ratos.service.transformer.SettingsFBTransformer;

import jakarta.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class SettingsFBService {

    private static final String SETTINGS_FB_NOT_FOUND_ID = "Requested SettingsFB is not found, setId = ";

    private final SettingsFBRepository settingsFBRepository;

    private final SettingsFBTransformer settingsFBTransformer;

    private final SettingsFBMapper settingsFBMapper;

    private final SecurityUtils securityUtils;

    //--------------------------------------------------------CRUD------------------------------------------------------
    @Transactional
    public Long save(@NonNull final SettingsFBInDto dto) {
        return settingsFBRepository.save(settingsFBTransformer.toEntity(dto)).getSettingsId();
    }

    @Transactional
    public void update(@NonNull final SettingsFBInDto dto) {
        if (dto.getSettingsId() == null) throw new RuntimeException("SettingsId is not found!");
        settingsFBRepository.save(settingsFBTransformer.toEntity(dto));
    }

    @Transactional
    public void deleteById(@NonNull final Long setId) {
        settingsFBRepository.findById(setId).get().setDeleted(true);
    }

    //---------------------------------------------------One (for update)-----------------------------------------------
    @Transactional(readOnly = true)
    public SettingsFBOutDto findOneForEdit(@NonNull final Long setId) {
        return settingsFBMapper.toDto(settingsFBRepository.findOneForEdit(setId)
                .orElseThrow(() -> new EntityNotFoundException(SETTINGS_FB_NOT_FOUND_ID + setId)));
    }

    //-----------------------------------------------------Staff table--------------------------------------------------
    @Transactional(readOnly = true)
    public Slice<SettingsFBOutDto> findAllByStaffId(@NonNull final Pageable pageable) {
        return settingsFBRepository.findAllByStaffId(securityUtils.getAuthStaffId(), pageable).map(settingsFBMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<SettingsFBOutDto> findAllByStaffIdAndNameLettersContains(@NonNull final String contains, @NonNull final Pageable pageable) {
        return settingsFBRepository.findAllByStaffIdAndNameLettersContains(securityUtils.getAuthStaffId(), contains, pageable).map(settingsFBMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<SettingsFBOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        return settingsFBRepository.findAllByDepartmentId(securityUtils.getAuthDepId(), pageable).map(settingsFBMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Slice<SettingsFBOutDto> findAllByDepartmentIdAndNameLettersContains(@NonNull final String contains, @NonNull final Pageable pageable) {
        return settingsFBRepository.findAllByDepartmentIdAndNameLettersContains(securityUtils.getAuthDepId(), contains, pageable).map(settingsFBMapper::toDto);
    }

    //-----------------------------------------------------------ADMIN--------------------------------------------------
    @Transactional(readOnly = true)
    public Slice<SettingsFBOutDto> findAllAdmin(@NonNull final Pageable pageable) {
        return settingsFBRepository.findAllAdmin(pageable).map(settingsFBMapper::toDto);
    }
}

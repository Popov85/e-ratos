package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.SchemeThemeSettings;
import ua.edu.ratos.dao.repository.SchemeThemeSettingsRepository;
import ua.edu.ratos.service.dto.in.SchemeThemeSettingsInDto;
import ua.edu.ratos.service.transformer.mapper.SchemeThemeMapper;

import java.util.Set;

@Service
@AllArgsConstructor
public class SchemeThemeSettingsService {

    private final SchemeThemeSettingsRepository schemeThemeSettingsRepository;

    private final SchemeThemeMapper schemeThemeMapper;

    //----------------------------------------------------CRUD----------------------------------------------------------
    @Transactional
    public Long save(@NonNull final SchemeThemeSettingsInDto dto) {
        SchemeThemeSettings schemeThemeSettings = schemeThemeMapper.toEntity(dto);
        return schemeThemeSettingsRepository.save(schemeThemeSettings).getSchemeThemeSettingsId();
    }

    @Transactional
    public void update(@NonNull final Long settingsId, @NonNull final SchemeThemeSettingsInDto dto) {
        if (!schemeThemeSettingsRepository.existsById(settingsId))
            throw new RuntimeException("Failed to update settings: ID does not exist");
        SchemeThemeSettings schemeThemeSettings = schemeThemeMapper.toEntity(dto);
        schemeThemeSettingsRepository.save(schemeThemeSettings);
    }

    // Used for checking if we are going to remove the last SchemeThemeSettings (prohibited)
    Set<SchemeThemeSettings> findAllBySchemeThemeId(@NonNull final Long schemeThemeId) {
        return schemeThemeSettingsRepository.findAllBySchemeThemeId(schemeThemeId);
    }

    // Do not call directly, call rather via SchemeThemeService's removeSettings() method
    void remove(@NonNull final Long settingsId) {
        schemeThemeSettingsRepository.deleteById(settingsId);
    }
}

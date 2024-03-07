package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.service.dto.in.SchemeThemeInDto;
import ua.edu.ratos.service.transformer.mapper.SchemeThemeSettingsMapper;
import ua.edu.ratos.service.transformer.SchemeThemeTransformer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class SchemeThemeTransformerImpl implements SchemeThemeTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SchemeThemeSettingsMapper schemeThemeSettingsMapper;

    @Override
    public SchemeTheme toEntity(@NonNull final Long schemeId, @NonNull SchemeThemeInDto dto) {
        SchemeTheme schemeTheme = new SchemeTheme();
        schemeTheme.setScheme(em.getReference(Scheme.class, schemeId));
        schemeTheme.setTheme(em.getReference(Theme.class, dto.getThemeId()));
        schemeTheme.setOrder(dto.getOrder());
        dto.getSettings().forEach(s-> {
            final SchemeThemeSettings set = modelMapper.map(s, SchemeThemeSettings.class);
            set.setType(em.getReference(QuestionType.class, s.getQuestionTypeId()));
            schemeTheme.addSchemeThemeSettings(set);
        });
        return schemeTheme;
    }

    @Override
    public SchemeTheme toEntity(@NonNull final SchemeThemeInDto dto) {
        SchemeTheme schemeTheme = new SchemeTheme();
        schemeTheme.setSchemeThemeId(dto.getSchemeThemeId());
        if (dto.getSchemeId()!=null) {
            schemeTheme.setScheme(em.getReference(Scheme.class, dto.getSchemeId()));
        }
        schemeTheme.setTheme(em.getReference(Theme.class, dto.getThemeId()));
        schemeTheme.setOrder(dto.getOrder());
        dto.getSettings().forEach(s-> {
            SchemeThemeSettings schemeThemeSettings = schemeThemeSettingsMapper.toEntity(s);
            schemeTheme.addSchemeThemeSettings(schemeThemeSettings);
        });
        return schemeTheme;
    }
}

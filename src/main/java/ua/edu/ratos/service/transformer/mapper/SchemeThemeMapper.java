package ua.edu.ratos.service.transformer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.ratos.dao.entity.SchemeTheme;
import ua.edu.ratos.dao.entity.SchemeThemeSettings;
import ua.edu.ratos.service.dto.in.SchemeThemeSettingsInDto;
import ua.edu.ratos.service.dto.out.SchemeThemeOutDto;
import ua.edu.ratos.service.dto.out.SchemeThemeSettingsOutDto;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
                QuestionTypeMapper.class,
                ReferenceMapper.class
        })
public interface SchemeThemeMapper {

    @Mapping(target = "themeId", source = "entity.theme.themeId")
    @Mapping(target = "theme", source = "entity.theme.name")
    SchemeThemeOutDto toDto(SchemeTheme entity);

    SchemeTheme toEntity(Long id);

    @Mapping(target = "schemeThemeId", source = "entity.schemeTheme.schemeThemeId")
    @Mapping(target = "questionTypeId", source = "entity.type.typeId")
    @Mapping(target = "type", source = "entity.type.abbreviation")
    SchemeThemeSettingsOutDto toDto(SchemeThemeSettings entity);

    @Mapping(target = "schemeTheme", source = "schemeThemeId")
    @Mapping(target = "type", source = "questionTypeId")
    SchemeThemeSettings toEntity(SchemeThemeSettingsInDto dto);
}

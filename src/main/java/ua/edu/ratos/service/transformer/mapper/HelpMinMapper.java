package ua.edu.ratos.service.transformer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.service.dto.out.HelpMinOutDto;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {ResourceMinMapper.class})
public interface HelpMinMapper {

    HelpMinOutDto toDto(Help entity);

    default HelpMinOutDto toDto(Optional<Help> help) {
        if (!help.isPresent()) return null;
        return toDto(help.get());
    }
}

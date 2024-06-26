package ua.edu.ratos.service.transformer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.ratos.dao.entity.Help;
import ua.edu.ratos.service.domain.HelpDomain;
import ua.edu.ratos.service.dto.out.HelpMinOutDto;
import ua.edu.ratos.service.dto.out.HelpOutDto;

import java.util.Optional;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {StaffMinMapper.class, ResourceMapper.class})
public interface HelpMapper {

    HelpOutDto toDto(Help entity);

    @Mapping(target = "resourceDomain", source = "resource")
    HelpDomain toDomain(Help entity);

    @Mapping(target = "resource", source = "resourceDomain")
    HelpMinOutDto toDto(HelpDomain domain);

    default HelpDomain toDomain(Optional<Help> help) {
        if (!help.isPresent()) return null;
        return toDomain(help.get());
    }
}

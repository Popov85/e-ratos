package ua.edu.ratos.service.transformer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ua.edu.ratos.dao.entity.Faculty;
import ua.edu.ratos.service.dto.in.FacultyInDto;
import ua.edu.ratos.service.dto.out.FacultyOutDto;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {OrganisationMapper.class, ReferenceMapper.class})
public interface FacultyMapper {

    FacultyOutDto toDto(Faculty entity);

    @Mapping(target = "organisation", source = "orgId")
    Faculty toEntity(FacultyInDto dto);

    Faculty toEntity(Long id);
}

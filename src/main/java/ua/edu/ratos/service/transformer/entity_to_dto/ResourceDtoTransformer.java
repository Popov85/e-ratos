package ua.edu.ratos.service.transformer.entity_to_dto;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.service.dto.out.ResourceOutDto;

@Slf4j
@Component
@AllArgsConstructor
public class ResourceDtoTransformer {

    private final StaffMinDtoTransformer staffMinDtoTransformer;

    public ResourceOutDto toDto(@NonNull final Resource entity) {
        return new ResourceOutDto()
                .setResourceId(entity.getResourceId())
                .setLink(entity.getLink())
                .setDescription(entity.getDescription())
                .setType(entity.getType())
                .setWidth(entity.getWidth())
                .setHeight(entity.getHeight())
                .setLastUsed(entity.getLastUsed())
                .setStaff(staffMinDtoTransformer.toDto(entity.getStaff()));
    }
}

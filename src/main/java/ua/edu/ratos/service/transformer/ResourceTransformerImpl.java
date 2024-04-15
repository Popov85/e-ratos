package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ResourceInDto;
import ua.edu.ratos.service.transformer.ResourceTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
public class ResourceTransformerImpl implements ResourceTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public Resource toEntity(@NonNull final ResourceInDto dto) {
        final Resource resource = modelMapper.map(dto, Resource.class);
        resource.setStaff((em.getReference(Staff.class, securityUtils.getAuthStaffId())));
        resource.setLastUsed(OffsetDateTime.now());
        return resource;
    }
}

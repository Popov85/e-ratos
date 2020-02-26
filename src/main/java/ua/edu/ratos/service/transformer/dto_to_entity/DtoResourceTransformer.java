package ua.edu.ratos.service.transformer.dto_to_entity;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ResourceInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
public class DtoResourceTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;


    @Transactional(propagation = Propagation.MANDATORY)
    public Resource toEntity(@NonNull final ResourceInDto dto) {
        final Resource resource = modelMapper.map(dto, Resource.class);
        resource.setStaff((em.getReference(Staff.class, securityUtils.getAuthStaffId())));
        resource.setLastUsed(OffsetDateTime.now());
        return resource;
    }
}

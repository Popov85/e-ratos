package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Group;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.GroupInDto;
import ua.edu.ratos.service.transformer.GroupTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class GroupTransformerImpl implements GroupTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final SecurityUtils securityUtils;

    private final ModelMapper modelMapper;

    @Override
    public Group toEntity(@NonNull final GroupInDto dto) {
        Group group = modelMapper.map(dto, Group.class);
        group.setCreated(LocalDateTime.now());
        group.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        group.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return group;
    }
}

package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Mode;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ModeInDto;
import ua.edu.ratos.service.transformer.ModeTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class ModeTransformerImpl implements ModeTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public Mode toEntity(@NonNull final ModeInDto dto) {
        Mode mode = modelMapper.map(dto, Mode.class);
        mode.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        mode.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return mode;
    }
}

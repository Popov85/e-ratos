package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Options;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.OptionsInDto;
import ua.edu.ratos.service.transformer.OptionsTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class OptionsTransformerImpl implements OptionsTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public Options toEntity(@NonNull final OptionsInDto dto) {
        Options options = modelMapper.map(dto, Options.class);
        options.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        options.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return options;
    }
}

package ua.edu.ratos.service.transformer.dto_to_entity;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Mode;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ModeInDto;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DtoModeTransformer {

    @PersistenceContext
    private EntityManager em;

    private ModelMapper modelMapper;

    private SecurityUtils securityUtils;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mode toEntity(@NonNull final ModeInDto dto) {
        Mode mode = modelMapper.map(dto, Mode.class);
        mode.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        mode.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return mode;
    }
}

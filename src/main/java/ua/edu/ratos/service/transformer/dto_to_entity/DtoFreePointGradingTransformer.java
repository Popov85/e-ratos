package ua.edu.ratos.service.transformer.dto_to_entity;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.grading.FreePointGrading;
import ua.edu.ratos.dao.entity.grading.Grading;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.FreePointGradingInDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DtoFreePointGradingTransformer {

    private static final Long GRADING_ID = 3L;

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

    public FreePointGrading toEntity(@NonNull final FreePointGradingInDto dto) {
        FreePointGrading entity = modelMapper.map(dto, FreePointGrading.class);
        entity.setGrading(em.getReference(Grading.class, GRADING_ID));
        entity.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        entity.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return entity;
    }
}
package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.grading.FreePointGrading;
import ua.edu.ratos.dao.entity.grading.Grading;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.FreePointGradingInDto;
import ua.edu.ratos.service.transformer.FreePointGradingTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class FreePointGradingTransformerImpl implements FreePointGradingTransformer {

    private static final Long GRADING_ID = 3L;

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public FreePointGrading toEntity(@NonNull final FreePointGradingInDto dto) {
        FreePointGrading entity = modelMapper.map(dto, FreePointGrading.class);
        entity.setGrading(em.getReference(Grading.class, GRADING_ID));
        entity.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        entity.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return entity;
    }
}

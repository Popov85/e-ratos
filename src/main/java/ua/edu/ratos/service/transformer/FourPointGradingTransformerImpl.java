package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.grading.FourPointGrading;
import ua.edu.ratos.dao.entity.grading.Grading;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.FourPointGradingInDto;
import ua.edu.ratos.service.transformer.FourPointGradingTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class FourPointGradingTransformerImpl implements FourPointGradingTransformer {

    private static final Long GRADING_ID = 1L;

    @PersistenceContext
    private final EntityManager em;

    private final SecurityUtils securityUtils;

    private final ModelMapper modelMapper;

    @Override
    public FourPointGrading toEntity(@NonNull final FourPointGradingInDto dto) {
        FourPointGrading entity = modelMapper.map(dto, FourPointGrading.class);
        entity.setGrading(em.getReference(Grading.class, GRADING_ID));
        entity.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        entity.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return entity;
    }
}

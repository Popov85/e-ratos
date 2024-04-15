package ua.edu.ratos.service.transformer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.grading.Grading;
import ua.edu.ratos.dao.entity.grading.TwoPointGrading;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.TwoPointGradingInDto;
import ua.edu.ratos.service.transformer.TwoPointGradingTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@AllArgsConstructor
public class TwoPointGradingTransformerImpl implements TwoPointGradingTransformer {

    private static final Long GRADING_ID = 2L;

    @PersistenceContext
    private final EntityManager em;

    private final ModelMapper modelMapper;

    private final SecurityUtils securityUtils;

    @Override
    public TwoPointGrading toEntity(@NonNull final TwoPointGradingInDto dto) {
        TwoPointGrading entity = modelMapper.map(dto, TwoPointGrading.class);
        entity.setGrading(em.getReference(Grading.class, GRADING_ID));
        entity.setStaff(em.getReference(Staff.class, securityUtils.getAuthStaffId()));
        entity.setDepartment(em.getReference(Department.class, securityUtils.getAuthDepId()));
        return entity;
    }
}

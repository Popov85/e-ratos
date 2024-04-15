package ua.edu.ratos.service.grading;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ratos.dao.entity.grading.FourPointGrading;
import ua.edu.ratos.dao.entity.grading.SchemeFourPoint;
import ua.edu.ratos.dao.repository.SchemeFourPointRepository;
import ua.edu.ratos.service.dto.out.grading.FourPointGradingOutDto;
import ua.edu.ratos.service.transformer.mapper.FourPointGradingMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class SchemeFourPointService implements SchemeGraderService {

    @PersistenceContext
    private final EntityManager em;

    private final SchemeFourPointRepository repository;

    private final FourPointGradingMapper fourPointGradingMapper;

    @Override
    public FourPointGradingOutDto save(long schemeId, long gradingDetailsId) {
        SchemeFourPoint schemeFourPoint = new SchemeFourPoint();
        schemeFourPoint.setSchemeId(schemeId);
        schemeFourPoint.setFourPointGrading(em.getReference(FourPointGrading.class, gradingDetailsId));
        schemeFourPoint = repository.save(schemeFourPoint);
        return fourPointGradingMapper.toDto(schemeFourPoint.getFourPointGrading());
    }

    @Override
    public FourPointGradingOutDto findDetails(long schemeId) {
        return fourPointGradingMapper.toDto(repository.findForDtoById(schemeId).getFourPointGrading());
    }

    @Override
    public void delete(long schemeId) {
        repository.deleteById(schemeId);
    }

    @Override
    public Long name() {
        return 1L;
    }

}

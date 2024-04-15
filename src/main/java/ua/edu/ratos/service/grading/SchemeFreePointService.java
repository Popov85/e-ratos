package ua.edu.ratos.service.grading;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ratos.dao.entity.grading.FreePointGrading;
import ua.edu.ratos.dao.entity.grading.SchemeFreePoint;
import ua.edu.ratos.dao.repository.SchemeFreePointRepository;
import ua.edu.ratos.service.dto.out.grading.FreePointGradingOutDto;
import ua.edu.ratos.service.transformer.mapper.FreePointGradingMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class SchemeFreePointService implements SchemeGraderService {

    @PersistenceContext
    private final EntityManager em;

    private final SchemeFreePointRepository repository;

    private final FreePointGradingMapper freePointGradingMapper;


    @Override
    public FreePointGradingOutDto save(long schemeId, long gradingDetailsId) {
        SchemeFreePoint schemeFreePoint = new SchemeFreePoint();
        schemeFreePoint.setSchemeId(schemeId);
        schemeFreePoint.setFreePointGrading(em.getReference(FreePointGrading.class, gradingDetailsId));
        schemeFreePoint = repository.save(schemeFreePoint);
        return freePointGradingMapper.toDto(schemeFreePoint.getFreePointGrading());
    }

    @Override
    public FreePointGradingOutDto findDetails(long schemeId) {
        return freePointGradingMapper.toDto(repository.findForDtoById(schemeId).getFreePointGrading());
    }

    @Override
    public void delete(long schemeId) {
        repository.deleteById(schemeId);
    }

    @Override
    public Long name() {
        return 3L;
    }
}

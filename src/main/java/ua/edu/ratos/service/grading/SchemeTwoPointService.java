package ua.edu.ratos.service.grading;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.edu.ratos.dao.entity.grading.SchemeTwoPoint;
import ua.edu.ratos.dao.entity.grading.TwoPointGrading;
import ua.edu.ratos.dao.repository.SchemeTwoPointRepository;
import ua.edu.ratos.service.dto.out.grading.TwoPointGradingOutDto;
import ua.edu.ratos.service.transformer.mapper.TwoPointGradingMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class SchemeTwoPointService implements SchemeGraderService {

    @PersistenceContext
    private final EntityManager em;

    private final SchemeTwoPointRepository repository;

    private final TwoPointGradingMapper twoPointGradingMapper;


    @Override
    public TwoPointGradingOutDto save(long schemeId, long gradingDetailsId) {
        SchemeTwoPoint schemeTwoPoint = new SchemeTwoPoint();
        schemeTwoPoint.setSchemeId(schemeId);
        schemeTwoPoint.setTwoPointGrading(em.getReference(TwoPointGrading.class, gradingDetailsId));
        schemeTwoPoint = repository.save(schemeTwoPoint);
        return twoPointGradingMapper.toDto(schemeTwoPoint.getTwoPointGrading());
    }

    @Override
    public TwoPointGradingOutDto findDetails(long schemeId) {
        return twoPointGradingMapper.toDto(repository.findForDtoById(schemeId).getTwoPointGrading());
    }

    @Override
    public void delete(long schemeId) {
        repository.deleteById(schemeId);
    }

    @Override
    public Long name() {
        return 2L;
    }
}

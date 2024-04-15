package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.grading.TwoPointGrading;
import ua.edu.ratos.dao.repository.TwoPointGradingRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.TwoPointGradingInDto;
import ua.edu.ratos.service.dto.out.grading.TwoPointGradingOutDto;
import ua.edu.ratos.service.transformer.mapper.TwoPointGradingMapper;
import ua.edu.ratos.service.transformer.TwoPointGradingTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TwoPointGradingService {

    private static final String ID_IS_NOT_INCLUDED = "TwoId is not included, reject update!";
    private static final String TWO_NOT_FOUND = "The requested TwoPointGrading is not found, twoId = ";
    private static final String DEFAULT_GRADINGS_CANNOT_BE_MODIFIED = "The default TwoPointGrading cannot be modified";

    private final TwoPointGradingRepository twoPointGradingRepository;

    private final TwoPointGradingMapper twoPointGradingMapper;

    private final TwoPointGradingTransformer twoPointGradingTransformer;

    private final SecurityUtils securityUtils;

    //-------------------------------------------------CRUD-------------------------------------------------------------
    @Transactional
    public TwoPointGradingOutDto save(@NonNull final TwoPointGradingInDto dto) {
        TwoPointGrading twoPointGrading = twoPointGradingRepository.save(twoPointGradingTransformer.toEntity(dto));
        return twoPointGradingMapper.toDto(twoPointGrading);
    }

    @Transactional
    public TwoPointGradingOutDto update(@NonNull final TwoPointGradingInDto dto) {
        Long twoId = dto.getTwoId();
        if (twoId == null || twoId == 0)
            throw new RuntimeException(ID_IS_NOT_INCLUDED + twoId);
        TwoPointGrading twoPointGrading = twoPointGradingRepository.findById(twoId)
                .orElseThrow(() -> new EntityNotFoundException(TWO_NOT_FOUND + twoId));
        if (twoPointGrading.isDefault())
            throw new RuntimeException(DEFAULT_GRADINGS_CANNOT_BE_MODIFIED );
        // Will merge actually
        TwoPointGrading updTwoPointGrading = twoPointGradingRepository.save(twoPointGradingTransformer.toEntity(dto));
        return twoPointGradingMapper.toDto(updTwoPointGrading);
    }

    @Transactional
    public void deleteById(@NonNull final Long twoId) {
        TwoPointGrading entity = twoPointGradingRepository.findById(twoId)
                .orElseThrow(() -> new EntityNotFoundException(TWO_NOT_FOUND + twoId));
        if (entity.isDefault()) throw new RuntimeException(DEFAULT_GRADINGS_CANNOT_BE_MODIFIED);
        entity.setDeleted(true);
    }

    //--------------------------------------------------------ONE-------------------------------------------------------
    @Transactional(readOnly = true)
    public TwoPointGradingOutDto findOneForEdit(@NonNull final Long twoId) {
        return twoPointGradingMapper.toDto(twoPointGradingRepository.findOneForEdit(twoId)
                .orElseThrow(() -> new EntityNotFoundException(TWO_NOT_FOUND + twoId)));
    }

    //------------------------------------------------------Default-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<TwoPointGradingOutDto> findAllDefault() {
        return twoPointGradingRepository.findAllDefault()
                .stream()
                .map(twoPointGradingMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff table/drop-down------------------------------------------
    @Transactional(readOnly = true)
    public Set<TwoPointGradingOutDto> findAllByDepartment() {
        return twoPointGradingRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(twoPointGradingMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Staff table/drop-down+default--------------------------------------
    @Transactional(readOnly = true)
    public Set<TwoPointGradingOutDto> findAllByDepartmentWithDefault() {
        Set<TwoPointGradingOutDto> result = findAllByDepartment();
        result.addAll(findAllDefault());
        return result;
    }
}

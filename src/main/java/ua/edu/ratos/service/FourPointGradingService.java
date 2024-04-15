package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.grading.FourPointGrading;
import ua.edu.ratos.dao.repository.FourPointGradingRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.FourPointGradingInDto;
import ua.edu.ratos.service.dto.out.grading.FourPointGradingOutDto;
import ua.edu.ratos.service.transformer.mapper.FourPointGradingMapper;
import ua.edu.ratos.service.transformer.FourPointGradingTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FourPointGradingService {

    private static final String ID_IS_NOT_INCLUDED = "FourId is not included, reject update!";
    private static final String FOUR_NOT_FOUND = "The requested FourPointGrading not found, fourId = ";
    private static final String DEFAULT_GRADINGS_CANNOT_BE_MODIFIED = "The default FourPointGrading cannot be modified";

    private final FourPointGradingRepository fourPointGradingRepository;

    private final FourPointGradingMapper fourPointGradingMapper;

    private final FourPointGradingTransformer fourPointGradingTransformer;

    private final SecurityUtils securityUtils;

    //-------------------------------------------------------CRUD-------------------------------------------------------
    @Transactional
    public FourPointGradingOutDto save(@NonNull final FourPointGradingInDto dto) {
        FourPointGrading fourPointGrading = fourPointGradingRepository.save(fourPointGradingTransformer.toEntity(dto));
        return fourPointGradingMapper.toDto(fourPointGrading);
    }

    @Transactional
    public FourPointGradingOutDto update(@NonNull final FourPointGradingInDto dto) {
        Long fourId = dto.getFourId();
        if (fourId == null || fourId == 0)
            throw new RuntimeException(ID_IS_NOT_INCLUDED + fourId);
        FourPointGrading fourPointGrading = fourPointGradingRepository.findById(fourId)
                .orElseThrow(() -> new EntityNotFoundException(FOUR_NOT_FOUND + fourId));
        if (fourPointGrading.isDefault())
            throw new RuntimeException(DEFAULT_GRADINGS_CANNOT_BE_MODIFIED );
        // Will merge actually
        FourPointGrading updFourPointGrading = fourPointGradingRepository.save(fourPointGradingTransformer.toEntity(dto));
        return fourPointGradingMapper.toDto(updFourPointGrading);
    }

    @Transactional
    public void deleteById(@NonNull final Long fourId) {
        FourPointGrading entity = fourPointGradingRepository
                .findById(fourId).orElseThrow(() -> new EntityNotFoundException(FOUR_NOT_FOUND + fourId));
        if (entity.isDefault()) throw new RuntimeException(DEFAULT_GRADINGS_CANNOT_BE_MODIFIED);
        entity.setDeleted(true);
    }

    //--------------------------------------------------One (for edit)--------------------------------------------------
    @Transactional(readOnly = true)
    public FourPointGradingOutDto findOneForEdit(@NonNull final Long fourId) {
        return fourPointGradingMapper.toDto(fourPointGradingRepository.findOneForEdit(fourId)
                .orElseThrow(()->new EntityNotFoundException(FOUR_NOT_FOUND+fourId)));
    }

    //------------------------------------------------------Default-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<FourPointGradingOutDto> findAllDefault() {
        return fourPointGradingRepository.findAllDefault()
                .stream()
                .map(fourPointGradingMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff table/drop-down------------------------------------------
    @Transactional(readOnly = true)
    public Set<FourPointGradingOutDto> findAllByDepartment() {
        return fourPointGradingRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(fourPointGradingMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Staff table/drop-down+default--------------------------------------
    @Transactional(readOnly = true)
    public Set<FourPointGradingOutDto> findAllByDepartmentWithDefault() {
        Set<FourPointGradingOutDto> result = findAllByDepartment();
        result.addAll(findAllDefault());
        return result;
    }
}

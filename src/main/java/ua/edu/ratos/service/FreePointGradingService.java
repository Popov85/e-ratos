package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.grading.FreePointGrading;
import ua.edu.ratos.dao.repository.FreePointGradingRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.FreePointGradingInDto;
import ua.edu.ratos.service.dto.out.grading.FreePointGradingOutDto;
import ua.edu.ratos.service.transformer.mapper.FreePointGradingMapper;
import ua.edu.ratos.service.transformer.FreePointGradingTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FreePointGradingService {

    private static final String ID_IS_NOT_INCLUDED = "FreeId is not included, reject update!";
    private static final String FREE_NOT_FOUND = "The requested FreePointGrading not found, freeId = ";
    private static final String DEFAULT_GRADINGS_CANNOT_BE_MODIFIED = "The default FreePointGrading cannot be modified";

    private final FreePointGradingRepository freePointGradingRepository;

    private final FreePointGradingMapper freePointGradingMapper;

    private final FreePointGradingTransformer freePointGradingTransformer;

    private final SecurityUtils securityUtils;

    //------------------------------------------------------CRUD--------------------------------------------------------
    @Transactional
    public FreePointGradingOutDto save(@NonNull final FreePointGradingInDto dto) {
        FreePointGrading freePointGrading = freePointGradingRepository.save(freePointGradingTransformer.toEntity(dto));
        return freePointGradingMapper.toDto(freePointGrading);
    }

    @Transactional
    public FreePointGradingOutDto update(@NonNull final FreePointGradingInDto dto) {
        Long freeId = dto.getFreeId();
        if (freeId == null || freeId == 0)
            throw new RuntimeException(ID_IS_NOT_INCLUDED + freeId);
        FreePointGrading freePointGrading = freePointGradingRepository.findById(freeId)
                .orElseThrow(() -> new EntityNotFoundException(FREE_NOT_FOUND + freeId));
        if (freePointGrading.isDefault())
            throw new RuntimeException(DEFAULT_GRADINGS_CANNOT_BE_MODIFIED );
        // Will merge actually
        FreePointGrading updFreePointGrading = freePointGradingRepository.save(freePointGradingTransformer.toEntity(dto));
        return freePointGradingMapper.toDto(updFreePointGrading);
    }

    @Transactional
    public void deleteById(@NonNull final Long freeId) {
        FreePointGrading entity = freePointGradingRepository.findById(freeId).orElseThrow(() -> new EntityNotFoundException(FREE_NOT_FOUND + freeId));
        if (entity.isDefault()) throw new RuntimeException(DEFAULT_GRADINGS_CANNOT_BE_MODIFIED);
        entity.setDeleted(true);
    }

    //--------------------------------------------------One (for update)------------------------------------------------
    @Transactional(readOnly = true)
    public FreePointGradingOutDto findOneForEdit(@NonNull final Long freeId) {
        return freePointGradingMapper.toDto(freePointGradingRepository.findOneForEdit(freeId)
                .orElseThrow(() -> new EntityNotFoundException(FREE_NOT_FOUND + freeId)));
    }

    //------------------------------------------------------Default-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<FreePointGradingOutDto> findAllDefault() {
        return freePointGradingRepository.findAllDefault()
                .stream()
                .map(freePointGradingMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------------Staff table/drop-down------------------------------------------
    @Transactional(readOnly = true)
    public Set<FreePointGradingOutDto> findAllByDepartment() {
        return freePointGradingRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(freePointGradingMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Staff table/drop-down+default--------------------------------------
    @Transactional(readOnly = true)
    public Set<FreePointGradingOutDto> findAllByDepartmentWithDefault() {
        Set<FreePointGradingOutDto> result = findAllByDepartment();
        result.addAll(findAllDefault());
        return result;
    }
}

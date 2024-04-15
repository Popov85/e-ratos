package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Mode;
import ua.edu.ratos.dao.repository.ModeRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.ModeInDto;
import ua.edu.ratos.service.dto.out.ModeOutDto;
import ua.edu.ratos.service.transformer.mapper.ModeMapper;
import ua.edu.ratos.service.transformer.ModeTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModeService {

    private static final String ID_IS_NOT_INCLUDED = "ModeId is not included, reject update!";
    private static final String MODE_NOT_FOUND = "The requested Mode is not found, modeId = ";
    private static final String DEFAULT_MODES_CANNOT_BE_MODIFIED = "Default modes cannot be modified!";

    private final ModeRepository modeRepository;

    private final ModeTransformer modeTransformer;

    private final ModeMapper modeMapper;

    private final SecurityUtils securityUtils;

    //----------------------------------------------------CRUD----------------------------------------------------------
    @Transactional
    public ModeOutDto save(@NonNull final ModeInDto dto) {
        Mode mode = modeRepository.save(modeTransformer.toEntity(dto));
        return modeMapper.toDto(mode);
    }

    @Transactional
    public ModeOutDto update(@NonNull final ModeInDto dto) {
        Long modeId = dto.getModeId();
        if (modeId == null || modeId == 0) throw new RuntimeException(ID_IS_NOT_INCLUDED);
        Mode mode = modeRepository.findById(modeId)
                .orElseThrow(() -> new EntityNotFoundException(MODE_NOT_FOUND + modeId));
        if (mode.isDefaultMode()) throw new RuntimeException(DEFAULT_MODES_CANNOT_BE_MODIFIED);
        Mode updMode = modeRepository.save(modeTransformer.toEntity(dto));
        return modeMapper.toDto(updMode);
    }

    @Transactional
    public void deleteById(@NonNull final Long modeId) {
        Mode mode = modeRepository.findById(modeId).orElseThrow(() -> new EntityNotFoundException(MODE_NOT_FOUND + modeId));
        if (mode.isDefaultMode()) throw new RuntimeException(DEFAULT_MODES_CANNOT_BE_MODIFIED);
        mode.setDeleted(true);
    }

    //-------------------------------------------------One (for edit)---------------------------------------------------
    @Transactional(readOnly = true)
    public ModeOutDto findOneForEdit(@NonNull final Long modeId) {
        return modeMapper.toDto(modeRepository.findOneForEdit(modeId)
                .orElseThrow(() -> new EntityNotFoundException(MODE_NOT_FOUND + modeId)));
    }

    //------------------------------------------------------Default-----------------------------------------------------
    @Transactional(readOnly = true)
    public Set<ModeOutDto> findAllDefault() {
        return modeRepository.findAllDefault()
                .stream()
                .map(modeMapper::toDto)
                .collect(Collectors.toSet());
    }

    //------------------------------------------------Staff table/drop-down---------------------------------------------
    @Transactional(readOnly = true)
    public Set<ModeOutDto> findAllByDepartment() {
        return modeRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(modeMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Staff table/drop-down+default--------------------------------------

    @Transactional(readOnly = true)
    public Set<ModeOutDto> findAllByDepartmentWithDefault() {
        Set<ModeOutDto> result = findAllByDepartment();
        result.addAll(findAllDefault());
        return result;
    }

    //-------------------------------------------------------ADMIN------------------------------------------------------
    @Transactional(readOnly = true)
    public Page<ModeOutDto> findAll(@NonNull final Pageable pageable) {
        return modeRepository.findAll(pageable).map(modeMapper::toDto);
    }
}

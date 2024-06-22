package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.ResultOfStudent;
import ua.edu.ratos.dao.repository.ResultOfStudentRepository;
import ua.edu.ratos.dao.repository.specs.SpecsFilter;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.out.criteria.ResultOfStudentForStaffOutDto;
import ua.edu.ratos.service.dto.out.criteria.ResultOfStudentSelfOutDto;
import ua.edu.ratos.service.transformer.mapper.ResultOfStudentForStaffMapper;
import ua.edu.ratos.service.transformer.mapper.ResultOfStudentSelfMapper;

import java.util.Map;

import static ua.edu.ratos.dao.repository.specs.ResultOfStudentSelfSpecs.byStudent;
import static ua.edu.ratos.dao.repository.specs.ResultOfStudentStaffSpecs.ofDepartment;
import static ua.edu.ratos.dao.repository.specs.ResultPredicatesUtils.hasSpecs;

@Service
@AllArgsConstructor
public class ResultOfStudentsService {

    private final ResultOfStudentRepository resultOfStudentRepository;

    private final ResultOfStudentSelfMapper resultOfStudentSelfMapper;

    private final ResultOfStudentForStaffMapper resultOfStudentForStaffMapper;

    private final SecurityUtils securityUtils;

    //------------------------------------------------------STUDENT-----------------------------------------------------
    @Transactional(readOnly = true)
    public Page<ResultOfStudentSelfOutDto> findAllByStudentId(@NonNull final Pageable pageable) {
        Specification<ResultOfStudent> specs = byStudent(securityUtils.getAuthUserId());
        return resultOfStudentRepository.findAll(specs, pageable).map(resultOfStudentSelfMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ResultOfStudentSelfOutDto> findAllByStudentIdAndSpecs(@NonNull final Map<String, SpecsFilter> dto, @NonNull final Pageable pageable) {
        Specification<ResultOfStudent> specs = byStudent(securityUtils.getAuthUserId()).and(hasSpecs(dto));
        return resultOfStudentRepository.findAll(specs, pageable).map(resultOfStudentSelfMapper::toDto);
    }


    //-------------------------------------------------------STAFF------------------------------------------------------

    @Transactional(readOnly = true)
    public ResultOfStudentForStaffOutDto findById(@NonNull final Long resultId) {
        return resultOfStudentRepository.findById(resultId).map(resultOfStudentForStaffMapper::toDto)
                .orElseThrow(()->new RuntimeException("Failed to execute search!"));
    }

    @Transactional(readOnly = true)
    public Page<ResultOfStudentForStaffOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        Specification<ResultOfStudent> specs = ofDepartment(securityUtils.getAuthDepId());
        return resultOfStudentRepository.findAll(specs, pageable).map(resultOfStudentForStaffMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ResultOfStudentForStaffOutDto> findAllByDepartmentIdAndSpecs(@NonNull final Map<String, SpecsFilter> dto, @NonNull final Pageable pageable) {
        Specification<ResultOfStudent> specs = ofDepartment(securityUtils.getAuthDepId()).and(hasSpecs(dto));
        return resultOfStudentRepository.findAll(specs, pageable).map(resultOfStudentForStaffMapper::toDto);
    }

    //----------------------------------------------------FAC-ADMIN (+)-------------------------------------------------
    @Transactional(readOnly = true)
    public Page<ResultOfStudentForStaffOutDto> findAllByDepartmentIdAdmin(@NonNull final Long depId, @NonNull final Pageable pageable) {
        Specification<ResultOfStudent> specs = ofDepartment(depId);
        return resultOfStudentRepository.findAll(specs, pageable).map(resultOfStudentForStaffMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ResultOfStudentForStaffOutDto> findAllByDepartmentIdAndSpecsAdmin(@NonNull final Long depId, @NonNull final Map<String, SpecsFilter> dto, @NonNull final Pageable pageable) {
        Specification<ResultOfStudent> specs = ofDepartment(depId).and(hasSpecs(dto));
        return resultOfStudentRepository.findAll(specs, pageable).map(resultOfStudentForStaffMapper::toDto);
    }

}

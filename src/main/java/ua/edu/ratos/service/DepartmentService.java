package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.repository.DepartmentRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.DepartmentInDto;
import ua.edu.ratos.service.dto.out.DepartmentMinOutDto;
import ua.edu.ratos.service.dto.out.DepartmentOutDto;
import ua.edu.ratos.service.transformer.mapper.DepartmentMapper;
import ua.edu.ratos.service.transformer.mapper.DepartmentMinMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DepartmentService {

    private static final String DEP_NOT_FOUND = "Requested department is not found, depId = ";

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    private final DepartmentMinMapper departmentMinMapper;

    private final SecurityUtils securityUtils;


    @Transactional
    public DepartmentOutDto save(@NonNull final DepartmentInDto dto) {
        Department department = departmentMapper.toEntity(dto);
        checkModificationPossibility(department);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Transactional
    public DepartmentOutDto update(@NonNull final DepartmentInDto dto) {
        if (dto.getDepId()==null)
            throw new RuntimeException("Failed to update, nullable depId field");
        Department department = departmentMapper.toEntity(dto);
        checkModificationPossibility(department);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Transactional
    public void updateName(@NonNull final Long depId, @NonNull final String name) {
        Department department = departmentRepository.findById(depId)
                .orElseThrow(() -> new EntityNotFoundException(DEP_NOT_FOUND + depId));
        checkModificationPossibility(department);
        department.setName(name);
    }

    @Transactional
    public void deleteById(@NonNull final Long depId) {
        Department department = departmentRepository.findById(depId)
                .orElseThrow(() -> new EntityNotFoundException(DEP_NOT_FOUND + depId));
        checkModificationPossibility(department);
        departmentRepository.delete(department);
        log.warn("Department is to be removed, depId= {}", depId);
    }

    /**
     * Only Global admin, org. admin and fac. admin have access to these CRUD operations.
     * Org admin and fac. admin cannot modify "foreign" departments and
     * we control it here. Whereas global admin can do it obviously!
     * @param dep Department to be modified
     */
    private void checkModificationPossibility(@NonNull final Department dep) {
        Long authOrgId = securityUtils.getAuthOrgId();
        Long authFacId = securityUtils.getAuthFacId();

        if ("ROLE_FAC-ADMIN".equals(securityUtils.getAuthRole()) && !authFacId.equals(dep.getFaculty().getFacId()))
            throw new SecurityException("You cannot modify department of a faculty you do not belong to!");

        if ("ROLE_ORG-ADMIN".equals(securityUtils.getAuthRole()) && !authOrgId.equals(dep.getFaculty().getOrganisation().getOrgId()))
            throw new SecurityException("You cannot modify department of an organisation you do not belong to!");
    }


    //---------------------------------------------------For drop-down--------------------------------------------------

    //-----------------------------------------------Fac. admin (min for drop down)-------------------------------------
    @Transactional(readOnly = true)
    public Set<DepartmentMinOutDto> findAllByFacIdForDropDown() {
        return departmentRepository.findAllByFacIdForDropDown(securityUtils.getAuthFacId())
                .stream()
                .map(departmentMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-----------------------------------------------Org. admin (min for drop down)-------------------------------------
    @Transactional(readOnly = true)
    public Set<DepartmentMinOutDto> findAllByFacIdForDropDown(@NonNull final Long facId) {
        return departmentRepository.findAllByFacIdForDropDown(facId)
                .stream()
                .map(departmentMinMapper::toDto)
                .collect(Collectors.toSet());
    }


    //--------------------------------------------------------For table-------------------------------------------------

    @Transactional(readOnly = true)
    public Set<DepartmentOutDto> findAllByFacIdForTable() {
        return departmentRepository.findAllByFacIdForTable(securityUtils.getAuthFacId())
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<DepartmentOutDto> findAllByOrgIdForTable() {
        return departmentRepository.findAllByOrgIdForTable(securityUtils.getAuthOrgId())
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<DepartmentOutDto> findAllByRatosForTable() {
        return departmentRepository.findAllByRatosForTable()
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toSet());
    }

}

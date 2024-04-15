package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.repository.StaffRepository;
import ua.edu.ratos.dao.repository.UserRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.StaffInDto;
import ua.edu.ratos.service.dto.in.StaffUpdInDto;
import ua.edu.ratos.service.dto.out.StaffMinOutDto;
import ua.edu.ratos.service.dto.out.StaffOutDto;
import ua.edu.ratos.service.transformer.mapper.StaffMapper;
import ua.edu.ratos.service.transformer.mapper.StaffMinMapper;
import ua.edu.ratos.service.transformer.StaffTransformer;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Technically we should allow creating/editing users of org, fac, dep ONLY by corresponding admins,
 * but for the sake of simplicity we omit implementing this functionality, and thus we trust front-end
 * to deal with possible contentions!
 */
@Slf4j
@Service
@AllArgsConstructor
public class StaffService {

    private static final String STAFF_NOT_FOUND = "The requested Staff is not found, staffId = ";

    private final UserRepository userRepository;

    private final StaffRepository staffRepository;

    private final StaffTransformer staffTransformer;

    private final StaffMapper staffMapper;

    private final StaffMinMapper staffMinMapper;

    private final SecurityUtils securityUtils;


    // Here comes logic to send email to provided address with password
    @Transactional
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public StaffOutDto save(@NonNull final StaffInDto dto) {
        Staff staff = staffTransformer.toEntity(dto);
        Staff result = staffRepository.save(staff);
        return staffMapper.toDto(result);
    }

    @Transactional
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public StaffOutDto update(@NonNull final StaffUpdInDto dto) {
        Long staffId = dto.getStaffId();
        if (staffId == null) throw new RuntimeException("Failed to update staff, staffId is required");
        Staff staff = staffRepository.findById(staffId).orElseThrow(() ->
                new EntityNotFoundException(STAFF_NOT_FOUND));
        staffTransformer.toEntity(staff, dto);
        return staffMapper.toDto(staff);
    }

    @Transactional
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public void delete(@NonNull final Long staffId) {
        staffRepository.deleteById(staffId);
        userRepository.deleteById(staffId);
    }

    //---------------------------------------------------One (min for DTOs)---------------------------------------------
    @Transactional(readOnly = true)
    public StaffMinOutDto findOneForDto(@NonNull final Long staffId) {
        Staff staff = staffRepository.findOneForEdit(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Staff is not found, staffId = " + staffId));
        return staffMinMapper.toDto(staff);
    }

    //---------------------------------------------------ALL for ADMIN-s------------------------------------------------
    @Transactional(readOnly = true)
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public Set<StaffOutDto> findAllByDepartmentId() {
        return staffRepository.findAllByDepartmentId(securityUtils.getAuthDepId())
                .stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Secured({"ROLE_FAC-ADMIN","ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public Set<StaffOutDto> findAllByFacultyId() {
        return staffRepository.findAllByFacultyId(securityUtils.getAuthFacId())
                .stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toSet());
    }


    @Transactional(readOnly = true)
    @Secured({"ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public Set<StaffOutDto> findAllByOrganisationId() {
        return staffRepository.findAllByOrganisationId(securityUtils.getAuthOrgId())
                .stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Secured({"ROLE_GLOBAL-ADMIN"})
    public Set<StaffOutDto> findAllByRatos() {
        return staffRepository.findAllByRatos()
                .stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toSet());
    }


    //---------------------------------------------SOLELY for future references-----------------------------------------

    @Transactional(readOnly = true)
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public Page<StaffOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        return staffRepository.findAllByDepartmentId(securityUtils.getAuthDepId(), pageable).map(staffMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public Page<StaffOutDto> findAllByDepartmentIdAndNameLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return staffRepository.findAllByDepartmentIdAndNameLettersContains(securityUtils.getAuthDepId(), letters, pageable).map(staffMapper::toDto);
    }

    //--------------------------------------------------------ADMIN-----------------------------------------------------
    @Transactional(readOnly = true)
    @Secured({"ROLE_GLOBAL-ADMIN"})
    public Page<StaffOutDto> findAllAdmin(@NonNull final Pageable pageable) {
        return staffRepository.findAllAdmin(pageable).map(staffMapper::toDto);
    }

}

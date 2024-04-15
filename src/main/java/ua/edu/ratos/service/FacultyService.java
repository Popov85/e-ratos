package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Faculty;
import ua.edu.ratos.dao.repository.FacultyRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.FacultyInDto;
import ua.edu.ratos.service.dto.out.FacultyMinOutDto;
import ua.edu.ratos.service.dto.out.FacultyOutDto;
import ua.edu.ratos.service.transformer.mapper.FacultyMapper;
import ua.edu.ratos.service.transformer.mapper.FacultyMinMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FacultyService {

    private static final String FAC_NOT_FOUND = "Requested faculty is not found, facId = ";

    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    private final FacultyMinMapper facultyMinMapper;

    private final SecurityUtils securityUtils;


    @Transactional
    public FacultyOutDto save(@NonNull final FacultyInDto dto) {
        checkModificationPossibility(dto.getOrgId());
        Faculty faculty = facultyMapper.toEntity(dto);
        faculty = facultyRepository.save(faculty);
        return facultyMapper.toDto(faculty);
    }

    @Transactional
    public FacultyOutDto update(@NonNull final FacultyInDto dto) {
        if (dto.getFacId()==null)
            throw new RuntimeException("Failed to update, nullable facId field");
        checkModificationPossibility(dto.getOrgId());
        Faculty faculty = facultyMapper.toEntity(dto);
        faculty = facultyRepository.save(faculty);
        return facultyMapper.toDto(faculty);
    }

    @Transactional
    public void updateName(@NonNull final Long facId, @NonNull final String name) {
        Faculty faculty = facultyRepository.findById(facId)
                .orElseThrow(() -> new EntityNotFoundException(FAC_NOT_FOUND + facId));
        checkModificationPossibility(faculty.getOrganisation().getOrgId());
        faculty.setName(name);
    }

    @Transactional
    public void deleteById(@NonNull final Long facId) {
        Faculty faculty = facultyRepository.findById(facId)
                .orElseThrow(() -> new EntityNotFoundException(FAC_NOT_FOUND + facId));
        checkModificationPossibility(faculty.getOrganisation().getOrgId());
        facultyRepository.delete(faculty);
        log.warn("Faculty is to be removed, facId= {}", facId);
    }

    /**
     * Only Global admin and org. admin have access to these CRUD operations.
     * Org admin cannot modify "foreign" faculties and
     * we control it here. Whereas global admin can do it obviously!
     * @param orgId
     */
    private void checkModificationPossibility(@NonNull final Long orgId) {
        Long authOrgId = securityUtils.getAuthOrgId();
        if ("ROLE_ORG-ADMIN".equals(securityUtils.getAuthRole()) && !authOrgId.equals(orgId))
            throw new SecurityException("You cannot modify faculty of an organisation you do not belong to!");
    }


    //-------------------------------------------------Staff (min for drop down)----------------------------------------
    @Transactional(readOnly = true)
    public Set<FacultyMinOutDto> findAllByOrgIdForDropDown() {
        return facultyRepository.findAllByOrgIdForDropDown(securityUtils.getAuthOrgId())
                .stream()
                .map(facultyMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    //---------------------------------------------Global admin (min for drop down)-------------------------------------

    @Transactional(readOnly = true)
    public Set<FacultyMinOutDto> findAllByOrgIdForDropDown(@NonNull final Long orgId) {
        return facultyRepository.findAllByOrgIdForDropDown(orgId)
                .stream()
                .map(facultyMinMapper::toDto)
                .collect(Collectors.toSet());
    }

    //--------------------------------------------------------Table-----------------------------------------------------

    //---------------------------------------------------Staff (for table)----------------------------------------------
    @Transactional(readOnly = true)
    public Set<FacultyOutDto> findAllByOrgIdForTable() {
        return facultyRepository.findAllByOrgIdForTable(securityUtils.getAuthOrgId())
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toSet());
    }

    //-------------------------------------------------Global admin (for table)-----------------------------------------

    @Transactional(readOnly = true)
    public Set<FacultyOutDto> findAllByOrgIdForTable(@NonNull final Long orgId) {
        return facultyRepository.findAllByOrgIdForTable(orgId)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<FacultyOutDto> findAllByRatosForTable() {
        return facultyRepository.findAllByRatosForTable()
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toSet());
    }
}

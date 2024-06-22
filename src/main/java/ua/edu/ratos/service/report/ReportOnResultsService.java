package ua.edu.ratos.service.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.ResultOfStudent;
import ua.edu.ratos.dao.repository.ResultOfStudentRepository;
import ua.edu.ratos.dao.repository.specs.SpecsFilter;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.StaffService;
import ua.edu.ratos.service.dto.in.report.ReportOnResultsInDto;
import ua.edu.ratos.service.dto.out.StaffMinOutDto;
import ua.edu.ratos.service.dto.out.criteria.ResultOfStudentForReportOutDto;
import ua.edu.ratos.service.dto.out.report.ReportOnResults;
import ua.edu.ratos.service.transformer.mapper.ResultOfStudentForReportMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.edu.ratos.dao.repository.specs.ResultOfStudentStaffSpecs.*;
import static ua.edu.ratos.dao.repository.specs.ResultPredicatesUtils.hasSpecs;

@Slf4j
@Service
@AllArgsConstructor
public class ReportOnResultsService {

    private final ResultOfStudentRepository resultOfStudentRepository;

    private final ResultOfStudentForReportMapper resultOfStudentForReportMapper;

    private final StaffService staffService;

    private final SecurityUtils securityUtils;


    @Transactional(readOnly = true)
    @Secured({"ROLE_LAB_ASSISTANT", "ROLE_INSTRUCTOR", "ROLE_DEP_ADMIN", "ROLE_FAC_ADMIN", "ROLE_ORG_ADMIN", "ROLE_GLOBAL_ADMIN"})
    public ReportOnResults getReportOnResults(@NonNull final ReportOnResultsInDto dto) {
        // Based on user role invoke corresponding report
        String role = securityUtils.getAuthRole();
        switch (role) {
            case "ROLE_DEP_ADMIN":
            case "ROLE_INSTRUCTOR":
            case "ROLE_LAB_ASSISTANT": {
                List<ResultOfStudentForReportOutDto> data = findAllForReportByDepartmentAndSpec(dto.getSpecs());
                log.debug("Created a report on results for dep. staff");
                return getReportOnContent(data);
            }
            case "ROLE_FAC_ADMIN": {
                List<ResultOfStudentForReportOutDto> data;
                // dep may be present, if not own faculty
                if (dto.getDepartment()==null) {
                    data = findAllForReportByFacultyAndSpec(dto.getSpecs());
                } else {
                    data = findAllForReportByDepartmentIdAndSpec(dto.getDepartment(), dto.getSpecs());
                }
                log.debug("Created a report on results for fac. admin");
                return getReportOnContent(data);
            }
            case "ROLE_ORG_ADMIN": {
                // fac/dep may be included, if neither - own organisation!
                List<ResultOfStudentForReportOutDto> data;
                if (dto.getDepartment()==null && dto.getFaculty()==null) {
                    data = findAllForReportByOrganisationAndSpec(dto.getSpecs());
                } else if (dto.getDepartment()!=null) {
                    data = findAllForReportByDepartmentIdAndSpec(dto.getDepartment(), dto.getSpecs());
                } else {
                    data = findAllForReportByFacultyIdAndSpec(dto.getFaculty(), dto.getSpecs());
                }
                log.debug("Created a report on results for org. admin");
                return getReportOnContent(data);
            }
            case "ROLE_GLOBAL_ADMIN": {
                // org/fac/dep may be included, if neither - own ratos instance!
                List<ResultOfStudentForReportOutDto> data;
                if (dto.getOrganisation() == null && dto.getDepartment()==null && dto.getFaculty()==null) {
                    data = findAllForReportByRatosAndSpec(dto.getSpecs());
                } else if (dto.getDepartment()!=null) {
                    data = findAllForReportByDepartmentIdAndSpec(dto.getDepartment(), dto.getSpecs());
                } else if (dto.getFaculty()!=null) {
                    data = findAllForReportByFacultyIdAndSpec(dto.getFaculty(), dto.getSpecs());
                } else {
                    data = findAllForReportByOrganisationIdAndSpec(dto.getOrganisation(), dto.getSpecs());
                }
                log.debug("Created a report on results for global admin");
                return getReportOnContent(data);
            }
            default:
                throw new SecurityException("Unrecognised role!");
        }
    }


    private List<ResultOfStudentForReportOutDto> findAllForReportByDepartmentAndSpec(@NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofDepartmentForReport(securityUtils.getAuthDepId()).and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ResultOfStudentForReportOutDto> findAllForReportByDepartmentIdAndSpec(@NonNull final Long depId, @NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofDepartmentForReport(depId).and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ResultOfStudentForReportOutDto> findAllForReportByFacultyAndSpec(@NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofFacultyForReport(securityUtils.getAuthFacId()).and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ResultOfStudentForReportOutDto> findAllForReportByFacultyIdAndSpec(@NonNull final Long facId, @NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofFacultyForReport(facId).and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ResultOfStudentForReportOutDto> findAllForReportByOrganisationAndSpec(@NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofOrganisationForReport(securityUtils.getAuthOrgId()).and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ResultOfStudentForReportOutDto> findAllForReportByOrganisationIdAndSpec(@NonNull final Long orgId, @NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofOrganisationForReport(orgId).and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<ResultOfStudentForReportOutDto> findAllForReportByRatosAndSpec(@NonNull final Map<String, SpecsFilter> dto) {
        Specification<ResultOfStudent> specs = ofRatosForReport().and(hasSpecs(dto));
        List<ResultOfStudent> results = resultOfStudentRepository.findAll(specs);
        return results
                .stream()
                .map(resultOfStudentForReportMapper::toDto)
                .collect(Collectors.toList());
    }

    private ReportOnResults getReportOnContent(List<ResultOfStudentForReportOutDto> data) {
        Long staffId = securityUtils.getAuthStaffId();
        StaffMinOutDto staff = staffService.findOneForDto(staffId);
        return new ReportOnResults(data, LocalDateTime.now(), staff);
    }
}

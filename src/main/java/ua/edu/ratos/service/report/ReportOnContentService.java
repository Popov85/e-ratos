package ua.edu.ratos.service.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.repository.*;
import ua.edu.ratos.dao.repository.lms.LMSCourseRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.StaffService;
import ua.edu.ratos.service.dto.in.report.ReportOnContentInDto;
import ua.edu.ratos.service.dto.out.report.OrgFacDep;
import ua.edu.ratos.service.dto.out.report.QuantityOfMaterials;
import ua.edu.ratos.service.dto.out.report.ReportOnContent;
import ua.edu.ratos.service.dto.out.report.ReportOnContentPiece;

import jakarta.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportOnContentService {

    private final DepartmentRepository departmentRepository;

    private final CourseRepository courseRepository;

    private final LMSCourseRepository lmsCourseRepository;

    private final SchemeRepository schemeRepository;

    private final ThemeRepository themeRepository;

    private final QuestionRepository questionRepository;

    private final StaffService staffService;

    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    @Secured({"ROLE_DEP-ADMIN", "ROLE_FAC-ADMIN", "ROLE_ORG-ADMIN", "ROLE_GLOBAL-ADMIN"})
    public ReportOnContent getReportOnContent(@NonNull final ReportOnContentInDto dto) {
        // Based on user role invoke corresponding report
        String role = securityUtils.getAuthRole();
        switch (role) {
            case "ROLE_DEP-ADMIN": {
                return getReportOnContentByDep(dto);
            }
            case "ROLE_FAC-ADMIN": {
                return getReportOnContentByFac(dto);
            }
            case "ROLE_ORG-ADMIN": {
                return getReportOnContentByOrg(dto);
            }
            case "ROLE_GLOBAL-ADMIN": {
                return getReportOnContentByRatos(dto);
            }
            default:
                throw new SecurityException("Unrecognised role!");
        }
    }


    private ReportOnContent getReportOnContentByDep(@NonNull final ReportOnContentInDto dto) {
        Long depId = securityUtils.getAuthDepId();
        Map<OrgFacDep, QuantityOfMaterials> map = new HashMap<>();
        if (dto.isCourses()) {
            Tuple tupleOfCourses = courseRepository.countCoursesByDepOfDepId(depId);
            addPieceOfReportOnCourse(map, tupleOfCourses);
        }
        if (dto.isLmsCourses()) {
            Tuple tupleOfLMSCourses = lmsCourseRepository.countLMSCoursesByDepOfDepId(depId);
            addPieceOfReportOnCourse(map, tupleOfLMSCourses);
        }
        if (dto.isSchemes()) {
            Tuple tupleOfSchemes = schemeRepository.countSchemesByDepOfDepId(depId);
            addPieceOfReportOnScheme(map, tupleOfSchemes);
        }
        if (dto.isThemes()) {
            Tuple tupleOfThemes = themeRepository.countThemesByDepOfDepId(depId);
            addPieceOfReportOnTheme(map, tupleOfThemes);
        }
        if (dto.isQuestions()) {
            Tuple tupleOfQuestions = questionRepository.countQuestionsByDepOfDepId(depId);
            addPieceOfReportOnQuestion(map, tupleOfQuestions);
        }
        return mapToReportConverter(map);
    }

    private ReportOnContent getReportOnContentByFac(@NonNull final ReportOnContentInDto dto) {
        Long facId = securityUtils.getAuthFacId();
        Map<OrgFacDep, QuantityOfMaterials> map = initMapForFac();

        if (dto.isCourses()) {
            Set<Tuple> tuplesOfCourses = courseRepository.countCoursesByDepOfFacId(facId);
            addPieceOfReportOnCourses(map, tuplesOfCourses);
        }
        if (dto.isLmsCourses()) {
            Set<Tuple> tuplesOfLMSCourses = lmsCourseRepository.countLMSCoursesByDepOfFacId(facId);
            addPieceOfReportOnLMSCourses(map, tuplesOfLMSCourses);
        }
        if (dto.isSchemes()) {
            Set<Tuple> tuplesOfSchemes = schemeRepository.countSchemesByDepOfFacId(facId);
            addPieceOfReportOnSchemes(map, tuplesOfSchemes);
        }
        if (dto.isThemes()) {
            Set<Tuple> tuplesOfThemes = themeRepository.countThemesByDepOfFacId(facId);
            addPieceOfReportOnThemes(map, tuplesOfThemes);
        }
        if (dto.isQuestions()) {
            Set<Tuple> tuplesOfQuestions = questionRepository.countQuestionsByDepOfFacId(facId);
            addPieceOfReportOnQuestions(map, tuplesOfQuestions);
        }
        return mapToReportConverter(map);
    }


    private ReportOnContent getReportOnContentByOrg(@NonNull final ReportOnContentInDto dto) {
        Long orgId = securityUtils.getAuthOrgId();
        Map<OrgFacDep, QuantityOfMaterials> map = initMapForOrg();
        if (dto.isCourses()) {
            Set<Tuple> tuplesOfCourses = courseRepository.countCoursesByDepOfOrgId(orgId);
            addPieceOfReportOnCourses(map, tuplesOfCourses);
        }
        if (dto.isLmsCourses()) {
            Set<Tuple> tuplesOfLMSCourses = lmsCourseRepository.countLMSCoursesByDepOfOrgId(orgId);
            addPieceOfReportOnLMSCourses(map, tuplesOfLMSCourses);
        }
        if (dto.isSchemes()) {
            Set<Tuple> tuplesOfSchemes = schemeRepository.countSchemesByDepOfOrgId(orgId);
            addPieceOfReportOnSchemes(map, tuplesOfSchemes);
        }
        if (dto.isThemes()) {
            Set<Tuple> tuplesOfThemes = themeRepository.countThemesByDepOfOrgId(orgId);
            addPieceOfReportOnThemes(map, tuplesOfThemes);
        }
        if (dto.isQuestions()) {
            Set<Tuple> tuplesOfQuestions = questionRepository.countQuestionsByDepOfOrgId(orgId);
            addPieceOfReportOnQuestions(map, tuplesOfQuestions);
        }
        return mapToReportConverter(map);
    }

    private ReportOnContent getReportOnContentByRatos(@NonNull final ReportOnContentInDto dto) {
        Map<OrgFacDep, QuantityOfMaterials> map = initMapForRatos();
        if (dto.isCourses()) {
            Set<Tuple> tuplesOfCourses = courseRepository.countCoursesByDepOfRatos();
            addPieceOfReportOnCourses(map, tuplesOfCourses);
        }
        if (dto.isLmsCourses()) {
            Set<Tuple> tuplesOfLMSCourses = lmsCourseRepository.countLMSCoursesByDepOfRatos();
            addPieceOfReportOnLMSCourses(map, tuplesOfLMSCourses);
        }
        if (dto.isSchemes()) {
            Set<Tuple> tuplesOfSchemes = schemeRepository.countSchemesByDepOfRatos();
            addPieceOfReportOnSchemes(map, tuplesOfSchemes);
        }
        if (dto.isThemes()) {
            Set<Tuple> tuplesOfThemes = themeRepository.countThemesByDepOfRatos();
            addPieceOfReportOnThemes(map, tuplesOfThemes);
        }
        if (dto.isQuestions()) {
            Set<Tuple> tuplesOfQuestions = questionRepository.countQuestionsByDepOfRatos();
            addPieceOfReportOnQuestions(map, tuplesOfQuestions);
        }
        return mapToReportConverter(map);
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnCourse(Map<OrgFacDep, QuantityOfMaterials> report, Tuple tupleOfCourses) {
        OrgFacDep orgFacDep = getOrgFacDep(tupleOfCourses);
        long courses = (long) tupleOfCourses.get("count");
        QuantityOfMaterials quantityOfMaterials = report.get(orgFacDep);
        if (quantityOfMaterials != null) {
            quantityOfMaterials.setQuantityOfCourses(courses);
        } else {
            quantityOfMaterials = new QuantityOfMaterials();
            quantityOfMaterials.setQuantityOfCourses(courses);
            report.put(orgFacDep, quantityOfMaterials);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnLMSCourse(Map<OrgFacDep, QuantityOfMaterials> report, Tuple tupleOfLMSCourses) {
        OrgFacDep orgFacDep = getOrgFacDep(tupleOfLMSCourses);
        long courses = (long) tupleOfLMSCourses.get("count");
        QuantityOfMaterials quantityOfMaterials = report.get(orgFacDep);
        if (quantityOfMaterials != null) {
            quantityOfMaterials.setQuantityOfLMSCourses(courses);
        } else {
            quantityOfMaterials = new QuantityOfMaterials();
            quantityOfMaterials.setQuantityOfLMSCourses(courses);
            report.put(orgFacDep, quantityOfMaterials);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnScheme(Map<OrgFacDep, QuantityOfMaterials> report, Tuple tupleOfSchemes) {
        OrgFacDep orgFacDep = getOrgFacDep(tupleOfSchemes);
        long schemes = (long) tupleOfSchemes.get("count");
        QuantityOfMaterials quantityOfMaterials = report.get(orgFacDep);
        if (quantityOfMaterials != null) {
            quantityOfMaterials.setQuantityOfSchemes(schemes);
        } else {
            quantityOfMaterials = new QuantityOfMaterials();
            quantityOfMaterials.setQuantityOfSchemes(schemes);
            report.put(orgFacDep, quantityOfMaterials);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnTheme(Map<OrgFacDep, QuantityOfMaterials> report, Tuple tupleOfThemes) {
        OrgFacDep orgFacDep = getOrgFacDep(tupleOfThemes);
        long themes = (long) tupleOfThemes.get("count");
        QuantityOfMaterials quantityOfMaterials = report.get(orgFacDep);
        if (quantityOfMaterials != null) {
            quantityOfMaterials.setQuantityOfThemes(themes);
        } else {
            quantityOfMaterials = new QuantityOfMaterials();
            quantityOfMaterials.setQuantityOfThemes(themes);
            report.put(orgFacDep, quantityOfMaterials);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnQuestion(Map<OrgFacDep, QuantityOfMaterials> report, Tuple tupleOfQuestions) {
        OrgFacDep orgFacDep = getOrgFacDep(tupleOfQuestions);
        long questions = (long) tupleOfQuestions.get("count");
        QuantityOfMaterials quantityOfMaterials = report.get(orgFacDep);
        if (quantityOfMaterials != null) {
            quantityOfMaterials.setQuantityOfQuestions(questions);
        } else {
            quantityOfMaterials = new QuantityOfMaterials();
            quantityOfMaterials.setQuantityOfQuestions(questions);
            report.put(orgFacDep, quantityOfMaterials);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnCourses(Map<OrgFacDep, QuantityOfMaterials> report, Set<Tuple> tupleOfCourses) {
        for (Tuple tuple : tupleOfCourses) {
            addPieceOfReportOnCourse(report, tuple);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnLMSCourses(Map<OrgFacDep, QuantityOfMaterials> report, Set<Tuple> tupleOfLMSCourses) {
        for (Tuple tuple : tupleOfLMSCourses) {
            addPieceOfReportOnLMSCourse(report, tuple);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnSchemes(Map<OrgFacDep, QuantityOfMaterials> report, Set<Tuple> tupleOfSchemes) {
        for (Tuple tuple : tupleOfSchemes) {
            addPieceOfReportOnScheme(report, tuple);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnThemes(Map<OrgFacDep, QuantityOfMaterials> report, Set<Tuple> tupleOfThemes) {
        for (Tuple tuple : tupleOfThemes) {
            addPieceOfReportOnTheme(report, tuple);
        }
        return report;
    }

    private Map<OrgFacDep, QuantityOfMaterials> addPieceOfReportOnQuestions(Map<OrgFacDep, QuantityOfMaterials> report, Set<Tuple> tupleOfQuestions) {
        for (Tuple tuple : tupleOfQuestions) {
            addPieceOfReportOnQuestion(report, tuple);
        }
        return report;
    }

    //------------------------------------------------Initializers------------------------------------------------------

    private Map<OrgFacDep, QuantityOfMaterials> initMapForFac() {
        Long facId = securityUtils.getAuthFacId();
        Map<OrgFacDep, QuantityOfMaterials> map = new HashMap<>();
        Set<OrgFacDep> allKeys = departmentRepository.findAllByFacIdForReport(facId);
        allKeys.forEach(k->map.put(k, new QuantityOfMaterials()));
        return map;
    }

    private Map<OrgFacDep, QuantityOfMaterials> initMapForOrg() {
        Long orgId = securityUtils.getAuthOrgId();
        Map<OrgFacDep, QuantityOfMaterials> map = new HashMap<>();
        Set<OrgFacDep> allKeys = departmentRepository.findAllByOrgIdForReport(orgId);
        allKeys.forEach(k->map.put(k, new QuantityOfMaterials()));
        return map;
    }

    private Map<OrgFacDep, QuantityOfMaterials> initMapForRatos() {
        Map<OrgFacDep, QuantityOfMaterials> map = new HashMap<>();
        Set<OrgFacDep> allKeys = departmentRepository.findAllByRatosForReport();
        allKeys.forEach(k->map.put(k, new QuantityOfMaterials()));
        return map;
    }

    //-------------------------------------------------Converters-------------------------------------------------------

    private ReportOnContent mapToReportConverter(Map<OrgFacDep, QuantityOfMaterials> map) {
        ReportOnContent report = new ReportOnContent();
        Set<ReportOnContentPiece> data =
                map.entrySet().stream().map(e -> {
                    OrgFacDep key = e.getKey();
                    QuantityOfMaterials value = e.getValue();
                    return mapToReportPieceConverter(key, value);
                }).collect(Collectors.toSet());

        report.setData(data);
        report.setRequestedBy(staffService.findOneForDto(securityUtils.getAuthStaffId()));
        report.setRequestedAt(LocalDateTime.now());
        return report;
    }

    private ReportOnContentPiece mapToReportPieceConverter(OrgFacDep orgFacDep, QuantityOfMaterials quantityOfMaterials) {
        ReportOnContentPiece piece = new ReportOnContentPiece();
        piece.setOrg(orgFacDep.getOrg());
        piece.setFac(orgFacDep.getFac());
        piece.setDep(orgFacDep.getDep());
        piece.setQuantityOfCourses(quantityOfMaterials.getQuantityOfCourses());
        piece.setQuantityOfLMSCourses(quantityOfMaterials.getQuantityOfLMSCourses());
        piece.setQuantityOfSchemes(quantityOfMaterials.getQuantityOfSchemes());
        piece.setQuantityOfThemes(quantityOfMaterials.getQuantityOfThemes());
        piece.setQuantityOfQuestions(quantityOfMaterials.getQuantityOfQuestions());
        return piece;
    }

    private OrgFacDep getOrgFacDep(Tuple tuple) {
        return new OrgFacDep(
                (String) tuple.get("org"),
                (String) tuple.get("fac"),
                (String) tuple.get("dep"));
    }
}

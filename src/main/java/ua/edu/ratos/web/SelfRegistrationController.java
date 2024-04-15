package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.SelfRegistrationService;
import ua.edu.ratos.service.dto.in.StudentInDto;
import ua.edu.ratos.service.dto.out.ClassMinOutDto;
import ua.edu.ratos.service.dto.out.FacultyMinOutDto;
import ua.edu.ratos.service.dto.out.OrganisationMinOutDto;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Set;

/**
 * Use this web for students self-registration option;
 * This is not always the case, as organisations may want to register their students
 * in a centralized way by staff workers. In this case, prohibit self-registration option in app settings.
 * Most probably, you would want to allow self-registration from inside an LMS,
 * where you already have pre-registered students. In this case use the corresponding flag in app settings.
 * @see AppProperties.Security
 */
@Slf4j
@RestController
@AllArgsConstructor
public class SelfRegistrationController {

    private final SelfRegistrationService selfRegistrationService;

    private final AppProperties appProperties;

    private final SecurityUtils securityUtils;

    @Getter
    @ToString
    @AllArgsConstructor
    private static class RegOptionsDto {
        private boolean isLms;
        private boolean isAllowed;
    }

    /**
     * Check if self-registration (and which type: lms or nonLms) is allowed by application settings;
     */
    @GetMapping(value="/self-registration/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegOptionsDto> selfRegistration() {
        // Get context;
        final boolean isLmsUser =  securityUtils.isLtiUser();
        // Get reg options for this context;
        final AppProperties.Security security = appProperties.getSecurity();
        final boolean isAllowed = isLmsUser ? security.isLmsRegistration() : security.isNonLmsRegistration();
        RegOptionsDto regOptions = new RegOptionsDto(isLmsUser, isAllowed);
        log.debug("Requested reg. options = {}", regOptions);
        return ResponseEntity.ok(regOptions);
    }

    //------------------------------------------non-LTI-registration----------------------------------------------------
    @GetMapping(value="/self-registration/organisations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<OrganisationMinOutDto> findAllOrganisations() {
        if (!appProperties.getSecurity().isNonLmsRegistration())
            throw new RuntimeException("Organisations info is hidden due to security considerations.");
        Set<OrganisationMinOutDto> result = selfRegistrationService.findAllOrganisations();
        log.debug("Organizations = {}", result);
        return result;
    }

    @GetMapping(value="/self-registration/faculties", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyMinOutDto> findAllFacultiesByOrgId(@RequestParam Long orgId) {
        if (!appProperties.getSecurity().isNonLmsRegistration())
            throw new RuntimeException("Faculties info is hidden due to security considerations.");
        return selfRegistrationService.findAllFacultiesByOrgId(orgId);
    }

    @GetMapping(value="/self-registration/classes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ClassMinOutDto> findAllClassesByFacId(@RequestParam Long facId) {
        if (!appProperties.getSecurity().isNonLmsRegistration())
            throw new RuntimeException("Classes info is hidden due to security considerations.");
        return selfRegistrationService.findAllClassesByFacId(facId);
    }

    /**
     * Here comes actual registration process outside an LMS
     * @param dto DTO with a student provided info
     * @return 201 created
     */
    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody StudentInDto dto) {
        if (!appProperties.getSecurity().isNonLmsRegistration())
            throw new RuntimeException("Registration outside of an LMS is restricted due to security considerations.");
        log.debug("StudentInDto = {}", dto);
        Long studId = selfRegistrationService.save(dto);
        log.debug("Saved Student, studId = {}", studId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/lab/students/{id}").buildAndExpand(studId).toUri();
        return ResponseEntity.created(location).build();
    }


    //----------------------------------------------LTI registration----------------------------------------------------
    // Use these endpoints only from LMS context
    @GetMapping(value="/lti/self-registration/organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> findOrganisation() {
        if (!appProperties.getSecurity().isLmsRegistration())
            throw new RuntimeException("Organisation info is hidden due to security considerations.");
        Long lmsId = securityUtils.getLmsId();
        Long orgId = selfRegistrationService.findOrganisation(lmsId);
        log.debug("lmsId = {}, orgId = {}", lmsId, orgId);
        return ResponseEntity.ok(orgId);
    }

    @GetMapping(value="/lti/self-registration/organisations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<OrganisationMinOutDto> findAllOrganisations2() {
        if (!appProperties.getSecurity().isLmsRegistration())
            throw new RuntimeException("Organisations info is hidden due to security considerations.");
        return selfRegistrationService.findAllOrganisations();
    }

    @GetMapping(value="/lti/self-registration/faculties", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyMinOutDto> findAllFacultiesByOrgId2(@RequestParam Long orgId) {
        if (!appProperties.getSecurity().isLmsRegistration())
            throw new RuntimeException("Faculties info is hidden due to security considerations.");
        return selfRegistrationService.findAllFacultiesByOrgId(orgId);
    }

    @GetMapping(value="/lti/self-registration/classes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ClassMinOutDto> findAllClassesByFacId2(@RequestParam Long facId) {
        if (!appProperties.getSecurity().isLmsRegistration())
            throw new RuntimeException("Classes info is hidden due to security considerations.");
        return selfRegistrationService.findAllClassesByFacId(facId);
    }

    /**
     * Here comes actual registration process from within LMS
     * Warning: LTI_ROLE must be present to reach this endpoint
     * @param dto DTO with a student provided info
     * @return 201 created
     */
    @PostMapping(value = {"/lti/sign-up"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save2(@Valid @RequestBody StudentInDto dto) {
        Long studId = selfRegistrationService.save(dto);
        log.debug("Saved LMS student, studId = {}", studId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/lab/students/{id}").buildAndExpand(studId).toUri();
        return ResponseEntity.created(location).build();
    }

}

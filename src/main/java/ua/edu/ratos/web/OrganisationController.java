package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.OrganisationService;
import ua.edu.ratos.service.dto.in.OrganisationInDto;
import ua.edu.ratos.service.dto.out.OrganisationMinOutDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/global-admin")
public class OrganisationController {

    private final OrganisationService organisationService;

    @PostMapping(value = "/organisations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long save(@Valid @RequestBody OrganisationInDto dto) {
        final Long orgId = organisationService.save(dto);
        log.debug("Saved Organisation, orgId = {}", orgId);
        return orgId;
    }

    @PutMapping(value = "/organisations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@Valid @RequestBody OrganisationInDto dto) {
        organisationService.update(dto);
        log.debug("Updated Organisation, orgId = {}", dto.getOrgId());
        return;
    }

    @PatchMapping(value = "/organisations/{orgId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable @Min(1) Long orgId, @RequestParam @NotBlank @Size(min = 3) String name) {
        organisationService.updateName(orgId, name);
        log.debug("Updated Organisation's name orgId = {}, new name = {}", orgId, name);
    }

    @DeleteMapping("/organisations/{orgId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long orgId) {
        organisationService.deleteById(orgId);
        log.debug("Deleted Organisation, orgId = {}", orgId);
    }

    //--------------------------------------------------Drop-downs------------------------------------------------------

    @GetMapping(value="/organisations-dropdown/all-org-by-ratos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<OrganisationMinOutDto> findAllOrganisationsForDropDown() {
        Set<OrganisationMinOutDto> result = organisationService.findAllOrganisationsForDropDown();
        return result;
    }
}

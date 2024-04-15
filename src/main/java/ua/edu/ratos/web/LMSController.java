package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.LMSService;
import ua.edu.ratos.service.dto.in.LMSInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.LMSMinOutDto;
import ua.edu.ratos.service.dto.out.LMSOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class LMSController {

    private final LMSService lmsService;

    @PostMapping(value = "/org-admin/lms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LMSOutDto> save(@Valid @RequestBody LMSInDto dto) {
        LMSOutDto lmsOutDto = lmsService.save(dto);
        log.debug("Saved LMS, lmsId = {}", lmsOutDto.getLmsId());
        return ResponseEntity.ok().body(lmsOutDto);
    }

    @PutMapping(value = "/org-admin/lms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LMSOutDto> update(@Valid @RequestBody LMSInDto dto) {
        LMSOutDto lmsOutDto = lmsService.update(dto);
        log.debug("Updated LMS, lmsId = {}", lmsOutDto.getLmsId());
        return ResponseEntity.ok().body(lmsOutDto);
    }

    @PatchMapping("/org-admin/lms/{lmsId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long lmsId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        lmsService.updateName(lmsId, name);
        log.debug("Updated LMS's name, lmsId = {}, new name = {}", lmsId, name);
    }

    @DeleteMapping("/org-admin/lms/{lmsId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long lmsId) {
        try {
            lmsService.deleteById(lmsId);
            log.debug("Deleted LMS, lmsId = {}", lmsId);
        } catch (Exception e) {
            lmsService.deleteByIdSoft(lmsId);
            log.debug("Soft deleted LMS, lmsId = {}", lmsId);
        }
    }

    //-----------------------------------------------Staff drop-down----------------------------------------------------
    @GetMapping(value = "/department/lms-dropdown/all-lms-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<LMSMinOutDto> findAllForDropdownByOrganisation() {
        return lmsService.findAllForDropdownByOrganisation();
    }

    //-------------------------------------------------Staff table------------------------------------------------------
    @GetMapping(value = "/department/lms-table/all-lms-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<LMSOutDto> findAllForTableByOrganisation() {
        return lmsService.findAllForTableByOrganisation();
    }

    @GetMapping(value = "/global-admin/lms-table/all-lms-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<LMSOutDto> findAllForTableByOrganisationId(@RequestParam final Long orgId) {
        return lmsService.findAllForTableByOrganisationId(orgId);
    }

}

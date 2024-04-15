package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.SchemeService;
import ua.edu.ratos.service.SchemeThemeService;
import ua.edu.ratos.service.dto.in.SchemeInDto;
import ua.edu.ratos.service.dto.in.SchemeThemeInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.SchemeMinOutDto;
import ua.edu.ratos.service.dto.out.SchemeOutDto;
import ua.edu.ratos.service.dto.out.SchemeShortOutDto;
import ua.edu.ratos.service.dto.out.SchemeThemeOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@SuppressWarnings("MVCPathVariableInspection")
@AllArgsConstructor
public class SchemeController {

    private final SchemeService schemeService;

    private final SchemeThemeService schemeThemeService;

    //----------------------------------------------------CRUD----------------------------------------------------------
    @PostMapping(value = "/instructor/schemes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeOutDto> save(@Valid @RequestBody SchemeInDto dto) {
        SchemeOutDto schemeOutDto = schemeService.save(dto);
        log.debug("Saved Scheme, schemeId = {}", schemeOutDto.getSchemeId());
        return ResponseEntity.ok(schemeOutDto);
    }

    @PutMapping(value = "/instructor/schemes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeOutDto> update(@Valid @RequestBody SchemeInDto dto) {
        SchemeOutDto schemeOutDto = schemeService.update(dto);
        log.debug("Updated Scheme, schemeId = {}", schemeOutDto.getSchemeId());
        return ResponseEntity.ok(schemeOutDto);
    }

    @GetMapping(value = "/instructor/schemes/{schemeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeOutDto> findOne(@PathVariable Long schemeId) {
        SchemeOutDto schemeOutDto = schemeService.findByIdForEdit(schemeId);
        log.debug("Retrieved Scheme, schemeId = {}", schemeOutDto.getSchemeId());
        return ResponseEntity.ok(schemeOutDto);
    }

    // -------------------------------------------------UPDATE fields---------------------------------------------------
    // Set of endpoints to immediately update any changes to Scheme object with AJAX from table
    @PatchMapping("/instructor/schemes/{schemeId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long schemeId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        schemeService.updateName(schemeId, name);
        log.debug("Updated Scheme's name, schemeId = {}, new name = {}", schemeId, name);
    }

    @PatchMapping("/instructor/schemes/{schemeId}/is-active")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateIsActive(@PathVariable Long schemeId, @RequestParam boolean isActive) {
        schemeService.updateIsActive(schemeId, isActive);
        log.debug("Updated Scheme's isActive flag, schemeId = {}, now the flag is = {}", schemeId, isActive);
    }

    @PatchMapping("/instructor/schemes/{schemeId}/is-lms-only")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateIsLmsOnly(@PathVariable Long schemeId, @RequestParam boolean isLmsOnly) {
        schemeService.updateIsLmsOnly(schemeId, isLmsOnly);
        log.debug("Updated Scheme's isLmsOnly flag, schemeId = {}, now the flag is = {}", schemeId, isLmsOnly);
    }

    /*@PutMapping("/instructor/schemes/{schemeId}/access")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateAccess(@PathVariable Long schemeId, @RequestParam Long accessId) {
        schemeService.updateAccess(schemeId, accessId);
        log.debug("Updated Scheme's Access, schemeId = {}, new accessId = {}", schemeId, accessId);
    }*/

    @DeleteMapping("/instructor/schemes/{schemeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long schemeId) {
        schemeService.deleteById(schemeId);
        log.debug("Deleted Scheme, schemeId = {}", schemeId);
    }

    //----------------------------------------------------Staff table---------------------------------------------------
    @GetMapping(value = "/department/schemes/all-schemes-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SchemeShortOutDto> findAllByDepartment() {
        return schemeService.findAllByDepartment();
    }

    //----------------------------------------------------Drop down-----------------------------------------------------

    @GetMapping(value="/department/schemes-dropdown/all-schemes-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SchemeMinOutDto> findAllForDropDownByDepartmentId() { return schemeService.findAllForDropdownByDepartmentId(); }

    @GetMapping(value="/fac-admin/schemes-dropdown/all-schemes-by-department", params = "depId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SchemeMinOutDto> findAllForDropDownByDepartmentId(@RequestParam final Long depId) { return schemeService.findAllForDropdownByDepartmentId(depId); }

    @GetMapping(value="/department/schemes-dropdown/all-schemes-by-course", params = "courseId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SchemeMinOutDto> findAllForDropDownByCourseId(@RequestParam final Long courseId) { return schemeService.findAllForDropdownByCourseId(courseId); }


    //--------------------------------------------------------GROUPS----------------------------------------------------
    // If the groups being sent in the request body are intended to be added to a collection, rather than replace,
    // I would suggest POST. If you intend to replace the existing tags, use PUT.
    /*@PostMapping(value = "/instructor/schemes/{schemeId}/groups/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addGroup(@PathVariable Long schemeId, @PathVariable Long groupId) {
        schemeService.addGroup(schemeId, groupId);
        log.debug("Added Group to Scheme, schemeId = {}, groupId = {}", schemeId, groupId);
    }

    @DeleteMapping(value = "/instructor/schemes/{schemeId}/groups/{groupId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeGroup(@PathVariable Long schemeId, @PathVariable Long groupId) {
        schemeService.removeGroup(schemeId, groupId);
        log.debug("Removed Group from Scheme, schemeId = {}, groupId = {}", schemeId, groupId);
    }*/

    // -------------------------------------------------------THEMES----------------------------------------------------
    @PostMapping(value = "/instructor/schemes/{schemeId}/themes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTheme(@PathVariable Long schemeId, @Valid @RequestBody SchemeThemeInDto dto) {
        final Long schemeThemeId = schemeThemeService.save(schemeId,  dto);
        log.debug("Added Theme to Scheme, created schemeThemeId = {} ", schemeThemeId);
        //URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(schemeThemeId).toUri();
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/instructor/schemes/{schemeId}/themes{schemeThemeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeThemeOutDto> findSchemeTheme(@PathVariable Long schemeThemeId) {
        SchemeThemeOutDto dto = schemeThemeService.findOne(schemeThemeId);
        log.debug("Retrieved SchemeTheme = {}", dto);
        return ResponseEntity.ok(dto);
    }

    /*@DeleteMapping("/instructor/schemes/{schemeId}/themes/{schemeThemeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeTheme(@PathVariable Long schemeId, @PathVariable Long schemeThemeId) {
        schemeService.removeTheme(schemeId, schemeThemeId);
        log.debug("Deleted schemeThemeId = {}", schemeThemeId);
    }*/

    /*@PutMapping("/instructor/schemes/{schemeId}/themes/re-order")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void reOrderThemes(@PathVariable Long schemeId, @RequestBody List<Long> schemeThemeIds) {
        schemeService.reOrderThemes(schemeId, schemeThemeIds);
        log.debug("Re-ordered Themes of schemeId = {}", schemeId);
    }*/

    // ------------------------------------------------------SETTINGS---------------------------------------------------
    /*@PostMapping(value = "/instructor/schemes/{schemeId}/themes/{schemeThemeId}/settings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSchemeThemeSettings(@Valid @RequestBody SchemeThemeSettingsInDto dto) {
        final Long schemeThemeSettingsId = schemeThemeSettingsService.save(dto);
        log.debug("Saved schemeThemeSettingsId = {} ", schemeThemeSettingsId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(schemeThemeSettingsId).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/instructor/schemes/{schemeId}/themes/{schemeThemeId}/settings/{schemeThemeSettingsId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateSchemeThemeSettings(@PathVariable Long schemeThemeSettingsId, @Valid @RequestBody SchemeThemeSettingsInDto dto) {
        schemeThemeSettingsService.update(schemeThemeSettingsId, dto);
        log.debug("Updated schemeThemeSettingsId = {}", schemeThemeSettingsId);
    }

    @DeleteMapping(value = "/instructor/schemes/{schemeId}/themes/{schemeThemeId}/settings/{schemeThemeSettingsId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeSchemeThemeSettings(@PathVariable Long schemeThemeId, @PathVariable Long schemeThemeSettingsId) {
        schemeThemeService.removeSettings(schemeThemeId, schemeThemeSettingsId);
        log.debug("Deleted schemeThemeSettingsId = {}", schemeThemeSettingsId);
    }
     */
}

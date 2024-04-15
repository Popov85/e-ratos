package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.FacultyService;
import ua.edu.ratos.service.dto.in.FacultyInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.FacultyMinOutDto;
import ua.edu.ratos.service.dto.out.FacultyOutDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping(value = "/org-admin/faculties", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FacultyOutDto> save(@Valid @RequestBody FacultyInDto dto) {
        FacultyOutDto facultyOutDto = facultyService.save(dto);
        log.debug("Saved Faculty, facId = {}", facultyOutDto.getFacId());
        return ResponseEntity.ok().body(facultyOutDto);
    }

    @PutMapping(value = "/org-admin/faculties", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FacultyOutDto> update(@Valid @RequestBody FacultyInDto dto) {
        FacultyOutDto facultyOutDto = facultyService.update(dto);
        log.debug("Updated Faculty, facId = {}", facultyOutDto.getFacId());
        return ResponseEntity.ok().body(facultyOutDto);
    }

    @PatchMapping(value = "/org-admin/faculties/{facId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable @Min(1) Long facId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        facultyService.updateName(facId, name);
        log.debug("Updated Faculty's name facId = {}, new name = {}", facId, name);
    }

    @DeleteMapping("/org-admin/faculties/{facId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long facId) {
        facultyService.deleteById(facId);
        log.debug("Deleted Faculty, facId = {}", facId);
    }

    //----------------------------------------------Min for drop-down---------------------------------------------------
    @GetMapping(value="/department/faculties-dropdown/all-fac-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyMinOutDto> findAllByOrgIdForDropDown() {
        return facultyService.findAllByOrgIdForDropDown();
    }

    @GetMapping(value="/global-admin/faculties-dropdown/all-fac-by-organisation", params = "orgId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyMinOutDto> findAllByOrgIdForDropDown(@RequestParam final Long orgId) {
        return facultyService.findAllByOrgIdForDropDown(orgId);
    }

    //---------------------------------------------------For table------------------------------------------------------
    @GetMapping(value="/org-admin/faculties-table/all-fac-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyOutDto> findAllByOrgIdForTable() {
        return facultyService.findAllByOrgIdForTable();
    }

    @GetMapping(value="/global-admin/faculties-table/all-fac-by-organisation", params = "orgId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyOutDto> findAllByOrgIdForTable(@RequestParam final Long orgId) {
        return facultyService.findAllByOrgIdForTable(orgId);
    }

    @GetMapping(value="/global-admin/faculties-table/all-fac-by-ratos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FacultyOutDto> findAllByRatosForTable() {
        return facultyService.findAllByRatosForTable();
    }
}

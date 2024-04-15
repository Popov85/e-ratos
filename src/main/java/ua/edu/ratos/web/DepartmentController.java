package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.DepartmentService;
import ua.edu.ratos.service.dto.in.DepartmentInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.DepartmentMinOutDto;
import ua.edu.ratos.service.dto.out.DepartmentOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping(value = "/fac-admin/departments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentOutDto> save(@Valid @RequestBody DepartmentInDto dto) {
        DepartmentOutDto departmentOutDto = departmentService.save(dto);
        log.debug("Saved Department, depId = {}", departmentOutDto.getDepId());
        return ResponseEntity.ok().body(departmentOutDto);
    }

    @PutMapping(value = "/fac-admin/departments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentOutDto> update(@Valid @RequestBody DepartmentInDto dto) {
        DepartmentOutDto departmentOutDto = departmentService.update(dto);
        log.debug("Updated Department, depId = {}", departmentOutDto.getDepId());
        return ResponseEntity.ok().body(departmentOutDto);
    }

    @PatchMapping(value = "/fac-admin/departments/{depId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long depId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        departmentService.updateName(depId, name);
        log.debug("Updated Department's name depId = {}, new name = {}", depId, name);
    }

    @DeleteMapping("/fac-admin/departments/{depId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long depId) {
        departmentService.deleteById(depId);
        log.debug("Deleted Department, depId = {}", depId);
    }

    //-----------------------------------------------------For drop-down------------------------------------------------

    @GetMapping(value="/fac-admin/departments-dropdown/all-dep-by-faculty", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DepartmentMinOutDto> findAllByFacIdForDropDown() {
        return departmentService.findAllByFacIdForDropDown();
    }

    @GetMapping(value="/org-admin/departments-dropdown/all-dep-by-faculty", params = "facId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DepartmentMinOutDto> findAllByFacIdForDropDown(@RequestParam final Long facId) {
        return departmentService.findAllByFacIdForDropDown(facId);
    }

    //-----------------------------------------------------For table----------------------------------------------------

    @GetMapping(value="/fac-admin/departments-table/all-dep-by-faculty", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DepartmentOutDto> findAllByFacIdForTable() {
        return departmentService.findAllByFacIdForTable();
    }

    @GetMapping(value="/org-admin/departments-table/all-dep-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DepartmentOutDto> findAllByOrgIdForTable() {
        return departmentService.findAllByOrgIdForTable();
    }

    @GetMapping(value="/global-admin/departments-table/all-dep-by-ratos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DepartmentOutDto> findAllByRatosForTable() {
        return departmentService.findAllByRatosForTable();
    }

}

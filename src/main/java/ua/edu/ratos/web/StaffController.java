package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.StaffService;
import ua.edu.ratos.service.dto.in.StaffInDto;
import ua.edu.ratos.service.dto.in.StaffUpdInDto;
import ua.edu.ratos.service.dto.out.StaffOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping(value = "/dep-admin/staff", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffOutDto> save(@Valid @RequestBody StaffInDto dto) {
        StaffOutDto staffOutDto = staffService.save(dto);
        log.debug("Saved Staff, staffId = {}", staffOutDto.getStaffId());
        return ResponseEntity.ok().body(staffOutDto);
    }

    @PutMapping(value = "/dep-admin/staff", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaffOutDto> update(@Valid @RequestBody StaffUpdInDto dto) {
        StaffOutDto staffOutDto = staffService.update(dto);
        log.debug("Updated Staff, staffId = {}", staffOutDto.getStaffId());
        return ResponseEntity.ok().body(staffOutDto);
    }

    @DeleteMapping("/dep-admin/staff/{staffId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long staffId) {
        staffService.delete(staffId);
        log.debug("Deleted Staff, staffId = {}", staffId);
    }

    //-----------------------------------------------Admin table--------------------------------------------------------
    @GetMapping(value = "/dep-admin/staff-table/all-staff-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<StaffOutDto> findAllByDepartmentId() {
        return staffService.findAllByDepartmentId();
    }

    @GetMapping(value = "/fac-admin/staff-table/all-staff-by-faculty", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<StaffOutDto> findAllByFacultyId() {
        return staffService.findAllByFacultyId();
    }

    @GetMapping(value = "/org-admin/staff-table/all-staff-by-organisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<StaffOutDto> findAllByOrganisationId() {
        return staffService.findAllByOrganisationId();
    }

    @GetMapping(value = "/global-admin/staff-table/all-staff-by-ratos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<StaffOutDto> findAllByRatosId() {
        return staffService.findAllByRatos();
    }



    //--------------------------------------------SOLELY for future refs------------------------------------------------

    @GetMapping(value = "/dep-admin/staff/by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<StaffOutDto> findAllByDepartmentId(@PageableDefault(sort = {"user.surname"}, value = 20) Pageable pageable) {
        return staffService.findAllByDepartmentId(pageable);
    }

    @GetMapping(value = "/dep-admin/staff/by-department", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<StaffOutDto> findAllByDepartmentIdAndNameLettersContains(@RequestParam String letters, @PageableDefault(sort = {"user.surname"}, value = 20) Pageable pageable) {
        return staffService.findAllByDepartmentIdAndNameLettersContains(letters, pageable);
    }
}

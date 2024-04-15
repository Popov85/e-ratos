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
import ua.edu.ratos.service.HelpService;
import ua.edu.ratos.service.dto.in.HelpInDto;
import ua.edu.ratos.service.dto.in.patch.LongInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.HelpOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class HelpController {

    private final HelpService helpService;

    @PostMapping(value = "/instructor/helps", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HelpOutDto> save(@Valid @RequestBody HelpInDto dto) {
        HelpOutDto helpOutDto = helpService.save(dto);
        log.debug("Saved Help = {}", helpOutDto);
        return ResponseEntity.ok(helpOutDto);
    }

    @PutMapping(value = "/instructor/helps", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HelpOutDto> update(@Valid @RequestBody HelpInDto dto) {
        HelpOutDto helpOutDto = helpService.update(dto);
        log.debug("Updated Help = {}", helpOutDto);
        return ResponseEntity.ok(helpOutDto);
    }

    @PatchMapping(value = "/instructor/helps/{helpId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long helpId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        helpService.updateName(helpId, name);
        log.debug("Updated Help's name, helpId = {}, new name = {}", helpId, name);
    }

    @PatchMapping(value = "/instructor/helps/{helpId}/help")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateHelp(@PathVariable Long helpId, @Valid @RequestBody StringInDto dto) {
        String help = dto.getValue();
        helpService.updateHelp(helpId, help);
        log.debug("Updated Help's help, helpId = {}, new help text = {}", helpId, help);
    }

    @PatchMapping(value = "/instructor/helps/{helpId}/resource")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateResource(@PathVariable Long helpId, @Valid @RequestBody LongInDto dto) {
        Long resId = dto.getValue();
        helpService.updateResource(helpId, resId);
        log.debug("Updated Help's resource, helpId = {}, new resId = {}", helpId, resId);
    }

    @DeleteMapping("/instructor/helps/{helpId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long helpId) {
        try {
            helpService.deleteById(helpId);
            log.debug("Deleted Help, helpId = {}", helpId);
        } catch (Exception e) {
            helpService.deleteByIdSoft(helpId);
            log.debug("Soft deleted Help, helpId = {}", helpId);
        }
    }

    //-----------------------------------------------------Staff--------------------------------------------------------
    @GetMapping(value = "/department/helps-table/all-helps-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<HelpOutDto> findAllByDepartment() {
        return helpService.findAllByDepartment();
    }

    //------------------------------------------Staff for future references---------------------------------------------
    @GetMapping(value = "/department/helps/by-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<HelpOutDto> findAllByStaffId(@PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return helpService.findAllByStaffId(pageable);
    }

    @GetMapping(value = "/department/helps/by-staff", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<HelpOutDto> findAllByStaffIdAndNameLettersContains(@RequestParam String letters, @PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return helpService.findAllByStaffIdAndNameLettersContains(letters, pageable);
    }

    @GetMapping(value = "/department/helps/by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<HelpOutDto> findAllByDepartmentId(@PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return helpService.findAllByDepartmentId(pageable);
    }

    @GetMapping(value = "/department/helps/by-department", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<HelpOutDto> findAllByDepartmentIdAndNameLettersContains(@RequestParam String letters, @PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return helpService.findAllByDepartmentIdAndNameLettersContains(letters, pageable);
    }
}

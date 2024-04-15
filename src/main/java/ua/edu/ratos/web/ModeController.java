package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.edu.ratos.service.ModeService;
import ua.edu.ratos.service.dto.in.ModeInDto;
import ua.edu.ratos.service.dto.out.ModeOutDto;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class ModeController {

    private final ModeService modeService;

    @PostMapping(value = "/instructor/modes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeOutDto> save(@Valid @RequestBody ModeInDto dto) {
        ModeOutDto modeOutDto = modeService.save(dto);
        log.debug("Saved Mode, modeId = {}", modeOutDto);
        return ResponseEntity.ok(modeOutDto);
    }

    @GetMapping(value = "/instructor/modes/{modeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeOutDto> findOne(@PathVariable Long modeId) {
        ModeOutDto modeOutDto = modeService.findOneForEdit(modeId);
        log.debug("Retrieved Mode = {}", modeOutDto);
        return ResponseEntity.ok(modeOutDto);
    }

    // Make sure to include modeId to DTO object
    @PutMapping(value = "/instructor/modes/{modeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<ModeOutDto> update(@PathVariable Long modeId, @Valid @RequestBody ModeInDto dto) {
        ModeOutDto modeOutDto = modeService.update(dto);
        log.debug("Updated Mode, modeId = {}", modeId);
        return ResponseEntity.ok(modeOutDto);
    }

    @DeleteMapping("/instructor/modes/{modeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long modeId) {
        modeService.deleteById(modeId);
        log.info("Delete Mode, modeId = {}", modeId);
    }

    //-----------------------------------------------------Default------------------------------------------------------
    @GetMapping(value = "/department/modes/all-default-modes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ModeOutDto> findAllDefault() {
        return modeService.findAllDefault();
    }

    //-----------------------------------------------Staff table/drop-down----------------------------------------------
    @GetMapping(value = "/department/modes/all-modes-by-department-with-default", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ModeOutDto> findAllByDepartmentWithDefault() {
        return modeService.findAllByDepartmentWithDefault();
    }
}

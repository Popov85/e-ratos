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

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class ModeController {

    private final ModeService modeService;

    @PostMapping(value = "/instructor/modes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody ModeInDto dto) {
        final Long modeId = modeService.save(dto);
        log.debug("Saved Mode, modeId = {}", modeId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(modeId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/instructor/modes/{modeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ModeOutDto> findOne(@PathVariable Long modeId) {
        ModeOutDto dto = modeService.findOneForEdit(modeId);
        log.debug("Retrieved Mode = {}", dto);
        return ResponseEntity.ok(dto);
    }

    // Make sure to include modeId to DTO object
    @PutMapping(value = "/instructor/modes/{modeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long modeId, @Valid @RequestBody ModeInDto dto) {
        modeService.update(dto);
        log.debug("Updated Mode, modeId = {}", modeId);
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

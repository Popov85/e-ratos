package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.edu.ratos.service.OptionsService;
import ua.edu.ratos.service.dto.in.OptionsInDto;
import ua.edu.ratos.service.dto.out.OptionsOutDto;

import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class OptionsController {

    private final OptionsService optionsService;

    @PostMapping(value = "/instructor/options", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Validated @RequestBody OptionsInDto dto) {
        final Long optId = optionsService.save(dto);
        log.debug("Saved Options, optId = {}", optId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/instructor/options/{optId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionsOutDto> findOne(@PathVariable Long optId) {
        OptionsOutDto dto = optionsService.findOneForEdit(optId);
        log.debug("Retrieved Options = {}", dto);
        return ResponseEntity.ok(dto);
    }

    // Make sure to include optId to DTO object
    @PutMapping(value = "/instructor/options/{optId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long optId, @Validated @RequestBody OptionsInDto dto) {
        optionsService.update(dto);
        log.debug("Updated Options, optId = {}", optId);
    }

    @DeleteMapping("/instructor/options/{optId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long optId) {
        optionsService.deleteById(optId);
        log.info("Deleted Options, optId = {}", optId);
    }

    //-----------------------------------------------------Default------------------------------------------------------
    @GetMapping(value = "/department/options/all-default-options", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<OptionsOutDto> findAllDefault() {
        return optionsService.findAllDefault();
    }

    //--------------------------------------------------Staff table-----------------------------------------------------

    @GetMapping(value="/department/options/all-options-by-department-with-default", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<OptionsOutDto> findAllByDepartment() {
        return optionsService.findAllByDepartmentWithDefault();
    }
}

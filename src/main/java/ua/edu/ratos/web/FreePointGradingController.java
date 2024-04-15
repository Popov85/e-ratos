package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.FreePointGradingService;
import ua.edu.ratos.service.dto.in.FreePointGradingInDto;
import ua.edu.ratos.service.dto.out.grading.FreePointGradingOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class FreePointGradingController {

    private final FreePointGradingService freePointGradingService;

    @PostMapping(value = "/instructor/free-point-gradings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FreePointGradingOutDto> save(@Valid @RequestBody FreePointGradingInDto dto) {
        FreePointGradingOutDto freePointGradingOutDto = freePointGradingService.save(dto);
        log.debug("Saved FreePointGrading, freeId = {}", freePointGradingOutDto.getFreeId());
        return ResponseEntity.ok(freePointGradingOutDto);
    }

    @GetMapping(value = "/instructor/free-point-gradings/{freeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FreePointGradingOutDto> findOne(@PathVariable Long freeId) {
        FreePointGradingOutDto freePointGradingOutDto = freePointGradingService.findOneForEdit(freeId);
        log.debug("Retrieved FreePointGrading = {}", freePointGradingOutDto);
        return ResponseEntity.ok(freePointGradingOutDto);
    }

    // Make sure to include freeId to DTO object
    @PutMapping(value = "/instructor/free-point-gradings/{freeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<FreePointGradingOutDto> update(@PathVariable Long freeId, @Valid @RequestBody FreePointGradingInDto dto) {
        FreePointGradingOutDto freePointGradingOutDto = freePointGradingService.update(dto);
        log.debug("Updated FreePointGrading, freeId = {}", freeId);
        return ResponseEntity.ok(freePointGradingOutDto);
    }

    @DeleteMapping("/instructor/free-point-gradings/{freeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long freeId) {
        freePointGradingService.deleteById(freeId);
        log.info("Deleted FreePointGrading, freeId = {}", freeId);
    }

    //----------------------------------------------------Default-------------------------------------------------------
    @GetMapping(value = "/department/free-point-gradings/all-default-free-point-gradings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FreePointGradingOutDto> findAllDefault() {
        return freePointGradingService.findAllDefault();
    }

    //--------------------------------------------------Staff table-----------------------------------------------------
    @GetMapping(value="/department/free-point-gradings/all-free-point-gradings-with-default", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FreePointGradingOutDto> findAllByDepartment() {
        return freePointGradingService.findAllByDepartmentWithDefault();
    }

}

package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.FourPointGradingService;
import ua.edu.ratos.service.dto.in.FourPointGradingInDto;
import ua.edu.ratos.service.dto.out.grading.FourPointGradingOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class FourPointGradingController {

    private final FourPointGradingService fourPointGradingService;

    @PostMapping(value = "/instructor/four-point-gradings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FourPointGradingOutDto> save(@Valid @RequestBody FourPointGradingInDto dto) {
        FourPointGradingOutDto fourPointGradingOutDto = fourPointGradingService.save(dto);
        log.debug("Saved FourPointGrading, fourId = {}", fourPointGradingOutDto.getFourId());
        return ResponseEntity.ok(fourPointGradingOutDto);
    }

    @GetMapping(value = "/instructor/four-point-gradings/{fourId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FourPointGradingOutDto> findOne(@PathVariable Long fourId) {
        FourPointGradingOutDto fourPointGradingOutDto = fourPointGradingService.findOneForEdit(fourId);
        log.debug("Retrieved FourPointGrading = {}", fourPointGradingOutDto);
        return ResponseEntity.ok(fourPointGradingOutDto);
    }

    // Make sure to include fourId to DTO object
    @PutMapping(value = "/instructor/four-point-gradings/{fourId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<FourPointGradingOutDto> update(@PathVariable Long fourId, @Valid @RequestBody FourPointGradingInDto dto) {
        FourPointGradingOutDto fourPointGradingOutDto = fourPointGradingService.update(dto);
        log.debug("Updated FourPointGrading, fourId = {}", fourId);
        return ResponseEntity.ok(fourPointGradingOutDto);
    }

    @DeleteMapping("/four-point-gradings/{fourId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long fourId) {
        fourPointGradingService.deleteById(fourId);
        log.info("Deleted FourPointGrading, fourId = {}", fourId);
    }

    //-----------------------------------------------------Default------------------------------------------------------
    @GetMapping(value = "/department/four-point-gradings/all-default-four-point-gradings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FourPointGradingOutDto> findAllDefault() {
        return fourPointGradingService.findAllDefault();
    }

    //--------------------------------------------------Staff table-----------------------------------------------------
    @GetMapping(value="/department/four-point-gradings/all-four-point-gradings-with-default", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<FourPointGradingOutDto> findAllByDepartment() {
        return fourPointGradingService.findAllByDepartmentWithDefault();
    }

}

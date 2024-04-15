package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.TwoPointGradingService;
import ua.edu.ratos.service.dto.in.TwoPointGradingInDto;
import ua.edu.ratos.service.dto.out.grading.TwoPointGradingOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class TwoPointGradingController {

    private final TwoPointGradingService twoPointGradingService;

    @PostMapping(value = "/instructor/two-point-gradings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TwoPointGradingOutDto> save(@Valid @RequestBody TwoPointGradingInDto dto) {
        TwoPointGradingOutDto twoPointGradingOutDto = twoPointGradingService.save(dto);
        log.debug("Saved TwoPointGrading, twoId = {}", twoPointGradingOutDto.getTwoId());
        return ResponseEntity.ok(twoPointGradingOutDto);
    }

    @GetMapping(value = "/instructor/two-point-gradings/{twoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TwoPointGradingOutDto> findOne(@PathVariable Long twoId) {
        TwoPointGradingOutDto twoPointGradingOutDto = twoPointGradingService.findOneForEdit(twoId);
        log.debug("Retrieved TwoPointGrading = {}", twoPointGradingOutDto);
        return ResponseEntity.ok(twoPointGradingOutDto);
    }

    // Make sure to include twoId to DTO object
    @PutMapping(value = "/instructor/two-point-gradings/{twoId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<TwoPointGradingOutDto> update(@PathVariable Long twoId, @Valid @RequestBody TwoPointGradingInDto dto) {
        TwoPointGradingOutDto twoPointGradingOutDto = twoPointGradingService.update(dto);
        log.debug("Updated TwoPointGrading, twoId = {}", twoId);
        return ResponseEntity.ok(twoPointGradingOutDto);
    }

    @DeleteMapping("/instructor/two-point-gradings/{twoId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long twoId) {
        twoPointGradingService.deleteById(twoId);
        log.info("Deleted TwoPointGrading, twoId = {}", twoId);
    }

    //-----------------------------------------------------Default------------------------------------------------------
    @GetMapping(value = "/department/two-point-gradings/all-default-two-point-gradings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<TwoPointGradingOutDto> findAllDefault() {
        return twoPointGradingService.findAllDefault();
    }

    //--------------------------------------------------Staff table-----------------------------------------------------
    @GetMapping(value="/department/two-point-gradings/all-two-point-gradings-with-default", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<TwoPointGradingOutDto> findAllByDepartment() {
        return twoPointGradingService.findAllByDepartmentWithDefault();
    }


}

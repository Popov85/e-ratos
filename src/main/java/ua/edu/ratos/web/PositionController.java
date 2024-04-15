package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.edu.ratos.service.PositionService;
import ua.edu.ratos.service.dto.in.PositionInDto;
import ua.edu.ratos.service.dto.out.PositionOutDto;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping(value = "/org-admin/positions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody PositionInDto dto) {
        Long posId = positionService.save(dto);
        log.debug("Saved Position, posId = {}", posId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(posId).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/org-admin/positions/{posId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long posId, @RequestParam String name) {
        positionService.updateName(posId, name);
        log.debug("Updated Position's name posId = {}, new name = {}", posId, name);
    }

    @DeleteMapping("/org-admin/positions/{posId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long posId) {
        positionService.deleteById(posId);
        log.debug("Deleted Position, posId = {}", posId);
    }


    // For DEP-ADMIN/ORG-ADMIN, etc. to support creating department/org. staff
    @GetMapping(value = "/dep-admin/positions-dropdown/all-pos-by-ratos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<PositionOutDto> findAll() {
        return positionService.findAll();
    }
}

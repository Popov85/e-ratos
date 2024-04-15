package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.edu.ratos.service.PhraseService;
import ua.edu.ratos.service.dto.in.PhraseInDto;
import ua.edu.ratos.service.dto.out.PhraseOutDto;

import jakarta.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/instructor")
@AllArgsConstructor
public class PhraseController {

    private final PhraseService phraseService;

    @PostMapping(value = "/phrases", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody PhraseInDto dto) {
        final Long phraseId = phraseService.save(dto);
        log.debug("Saved Phrase, phraseId = {}", phraseId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(phraseId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/phrases/{phraseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhraseOutDto> findAllByStaffId(@PathVariable Long phraseId) {
        PhraseOutDto result = phraseService.findOneForUpdate(phraseId);
        log.debug("Retrieved Phrase = {}", result);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/phrases/{phraseId}/phrase")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePhrase(@PathVariable Long phraseId, @RequestParam String phrase) {
        phraseService.updatePhrase(phraseId, phrase);
        log.debug("Updated Phrase's phrase, phraseId = {}, new phrase = {}", phraseId, phrase);
    }

    @PutMapping(value = "/phrases/{phraseId}/resource")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateResource(@PathVariable Long phraseId, @RequestParam Long resId) {
        phraseService.updateResource(phraseId, resId);
        log.debug("Updated Phrase's resource, phraseId = {}, new resId = {}", phraseId, resId);
    }

    @DeleteMapping("/phrases/{phraseId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long phraseId) {
        phraseService.deleteById(phraseId);
        log.debug("Deleted Phrase, phraseId = {}", phraseId);
    }

    //------------------------------------------------------Staff table-------------------------------------------------
    @GetMapping(value = "/phrases/by-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PhraseOutDto> findAllByStaffId(@PageableDefault(sort = {"lastUsed"}, direction = Sort.Direction.DESC, value = 50) Pageable pageable) {
        return phraseService.findAllByStaffId(pageable);
    }

    @GetMapping(value = "/phrases/by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PhraseOutDto> findAllByDepartmentId(@PageableDefault(sort = {"lastUsed"}, direction = Sort.Direction.DESC, value = 50) Pageable pageable) {
        return phraseService.findAllByDepartmentId(pageable);
    }

    @GetMapping(value = "/phrases/by-staff", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PhraseOutDto> findAllByStaffIdAndPhraseLettersContains(@RequestParam String letters, @PageableDefault(sort = {"phrase"}, value = 50) Pageable pageable) {
        return phraseService.findAllByStaffIdAndPhraseLettersContains(letters, pageable);
    }

    @GetMapping(value = "/phrases/by-department", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PhraseOutDto> findAllByDepIdAndLettersStarts(@RequestParam String letters, @PageableDefault(sort = {"phrase"}, value = 50) Pageable pageable) {
        return phraseService.findAllByDepartmentIdAndPhraseLettersContains(letters, pageable);
    }

}

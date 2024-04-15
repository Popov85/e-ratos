package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.patch.BooleanInDto;
import ua.edu.ratos.service.dto.in.patch.LevelInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.question.QuestionMCQOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    @PatchMapping(value = "/instructor/questions/{questionId}/name", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long questionId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        questionService.updateName(questionId, name);
        log.debug("Updated Question name, questionId = {}, name = {}", questionId, name);
    }

    @PatchMapping(value = "/instructor/questions/{questionId}/level", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateLevel(@PathVariable Long questionId, @Valid @RequestBody LevelInDto dto) {
        Byte level = dto.getValue();
        questionService.updateLevel(questionId, level);
        log.debug("Updated Question level, questionId = {}, level = {}", questionId, level);
    }

    @PatchMapping(value = "/instructor/questions/{questionId}/required", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRequired(@PathVariable Long questionId, @Valid @RequestBody BooleanInDto dto) {
        Boolean required = dto.getValue();
        questionService.updateRequired(questionId, required);
        log.debug("Updated Question required, questionId = {}, required = {}", questionId, required);
    }

    @PatchMapping(value = "/instructor/questions/{questionId}/associate-with-help/{helpId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void associateWithHelp(@PathVariable Long questionId, @PathVariable Long helpId) {
        questionService.associateWithHelp(questionId, helpId);
        log.debug("Associated question with help, questionId = {}, helpId = {}", questionId, helpId);
    }

    @PatchMapping(value = "/instructor/questions/{questionId}/disassociate-with-help")
    @ResponseStatus(value = HttpStatus.OK)
    public void disassociateWithHelp(@PathVariable Long questionId) {
        questionService.disassociateWithHelp(questionId);
        log.debug("Disassociated question with help, questionId = {}", questionId);
    }

    @PatchMapping(value = "/instructor/questions/{questionId}/associate-with-resource/{resourceId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void associateWithResource(@PathVariable Long questionId, @PathVariable Long resourceId) {
        questionService.associateWithResource(questionId, resourceId);
        log.debug("Associated question with resource, questionId = {}, resourceId = {}", questionId, resourceId);
    }

    @PatchMapping(value = "/instructor/questions/{questionId}/disassociate-with-resource")
    @ResponseStatus(value = HttpStatus.OK)
    public void disassociateWithResource(@PathVariable Long questionId) {
        questionService.disassociateWithResource(questionId);
        log.debug("Disassociated question with resource, questionId = {}", questionId);
    }

    @DeleteMapping("/instructor/questions/{questionId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long questionId) {
        questionService.deleteById(questionId);
        log.debug("Deleted Question, questionId = {}", questionId);
    }

    //--------------------------------------------------Staff table-----------------------------------------------------

    @GetMapping(value = "/department/questions-mcq-table/all-mcq-by-theme", params = "themeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<QuestionMCQOutDto> findAllMCQForEditByThemeId(@RequestParam Long themeId) {
        return questionService.findAllMCQForEditByThemeId(themeId);
    }
    // TODO: add more GET methods for other question types
}

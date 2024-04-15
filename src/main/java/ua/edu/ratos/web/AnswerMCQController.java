package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.AnswerMCQService;
import ua.edu.ratos.service.dto.in.AnswerMCQInDto;
import ua.edu.ratos.service.dto.out.answer.AnswerMCQOutDto;
import ua.edu.ratos.service.validator.AnswerMCQInDtoValidator;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/instructor")
@AllArgsConstructor
public class AnswerMCQController {

    private final AnswerMCQService answerService;

    private final AnswerMCQInDtoValidator validator;

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping(value = "/questions/{questionId}/answer-mcq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerMCQOutDto> save(@PathVariable Long questionId, @Valid @RequestBody AnswerMCQInDto dto) {
        AnswerMCQOutDto answerMCQOutDto = answerService.save(questionId, dto);
        log.debug("Saved Answer MCQ, answerId = {}", answerMCQOutDto.getAnswerId());
        return ResponseEntity.ok(answerMCQOutDto);
    }

    @PutMapping(value = "/questions/{questionId}/answers-mcq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<AnswerMCQOutDto> update(@PathVariable Long questionId, @Valid @RequestBody AnswerMCQInDto dto) {
        AnswerMCQOutDto answerMCQOutDto = answerService.update(questionId, dto);
        log.debug("Updated Answer MCQ, answerId = {}", answerMCQOutDto.getAnswerId());
        return ResponseEntity.ok(answerMCQOutDto);
    }

    @DeleteMapping("/questions/answers-mcq/{answerId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long answerId) {
        answerService.deleteById(answerId);
        log.debug("Deleted Answer MCQ answerId = {}", answerId);
    }
}

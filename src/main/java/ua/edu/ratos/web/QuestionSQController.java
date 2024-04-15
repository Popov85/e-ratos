package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionSQInDto;
import ua.edu.ratos.service.dto.out.question.QuestionSQOutDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class QuestionSQController {

    private final QuestionService questionService;

    @PostMapping(value = "/instructor/questions-sq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionSQOutDto> save(@Valid @RequestBody QuestionSQInDto dto) {
        QuestionSQOutDto questionSQOutDto = questionService.save(dto);
        log.debug("Saved Question SQ, questionId = {}", questionSQOutDto.getQuestionId());
        return ResponseEntity.ok(questionSQOutDto);
    }

}

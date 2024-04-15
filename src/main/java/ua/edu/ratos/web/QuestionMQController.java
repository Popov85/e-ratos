package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionMQInDto;
import ua.edu.ratos.service.dto.out.question.QuestionMQOutDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class QuestionMQController {

    private final QuestionService questionService;

    @PostMapping(value = "/instructor/questions-mq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionMQOutDto> save(@Valid @RequestBody QuestionMQInDto dto) {
        QuestionMQOutDto questionMQOutDto = questionService.save(dto);
        log.debug("Saved Question MQ, questionId = {}", questionMQOutDto.getQuestionId());
        return ResponseEntity.ok(questionMQOutDto);
    }

}

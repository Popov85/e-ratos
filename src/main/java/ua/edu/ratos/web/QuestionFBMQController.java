package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionFBMQInDto;
import ua.edu.ratos.service.dto.out.question.QuestionFBMQOutDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class QuestionFBMQController {

    private final QuestionService questionService;

    @PostMapping(value = "/instructor/questions-fbmq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionFBMQOutDto> save(@Valid @RequestBody QuestionFBMQInDto dto) {
        QuestionFBMQOutDto questionFBMQOutDto = questionService.save(dto);
        log.debug("Saved Question FBMQ, questionId = {}", questionFBMQOutDto.getQuestionId());
        return ResponseEntity.ok(questionFBMQOutDto);
    }

}

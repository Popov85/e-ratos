package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.dto.in.QuestionFBSQInDto;
import ua.edu.ratos.service.dto.out.question.QuestionFBSQOutDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class QuestionFBSQController {

    private final QuestionService questionService;

    @PostMapping(value = "/instructor/questions-fbsq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionFBSQOutDto> save(@Valid @RequestBody QuestionFBSQInDto dto) {
        QuestionFBSQOutDto questionFBSQOutDto = questionService.save(dto);
        log.debug("Saved Question FBSQ, questionId = {}", questionFBSQOutDto.getQuestionId());
        return ResponseEntity.ok(questionFBSQOutDto);
    }

}

package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.AnswerFBSQService;
import ua.edu.ratos.service.dto.in.AnswerFBSQInDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/instructor")
@AllArgsConstructor
public class AnswerFBSQController {

    private final AnswerFBSQService answerService;

    // For this particular question questionType: questionId = answerId (single answer!)
    @PutMapping("/questions/{questionId}/answer-fbsq")
    public void update(@PathVariable Long questionId, @Valid @RequestBody AnswerFBSQInDto dto) {
        answerService.update(dto);
        log.debug("Updated Answer FBSQ, questionId = {}", questionId);
    }
}

package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.ratos.service.QuestionService;
import ua.edu.ratos.service.QuestionsFileParserService;
import ua.edu.ratos.service.dto.in.FileInDto;
import ua.edu.ratos.service.dto.in.QuestionMCQInDto;
import ua.edu.ratos.service.dto.out.QuestionsParsingResultOutDto;
import ua.edu.ratos.service.dto.out.question.QuestionMCQOutDto;
import ua.edu.ratos.service.validator.QuestionMCQInDtoValidator;

import jakarta.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
public class QuestionMCQController {

    private final QuestionService questionService;

    private final QuestionsFileParserService questionsFileParserService;

    private final QuestionMCQInDtoValidator validator;

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping(value = "/instructor/questions-mcq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionMCQOutDto> save(@Valid @RequestBody QuestionMCQInDto dto) {
        QuestionMCQOutDto questionMCQOutDto = questionService.save(dto);
        log.debug("Saved Question MCQ, questionId = {}", questionMCQOutDto.getQuestionId());
        return ResponseEntity.ok(questionMCQOutDto);
}

    @PutMapping(value = "/instructor/questions-mcq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionMCQOutDto> update(@Valid @RequestBody QuestionMCQInDto dto) {
        QuestionMCQOutDto questionMCQOutDto = questionService.update(dto);
        log.debug("Updated Question MCQ, questionId = {}", questionMCQOutDto.getQuestionId());
        return ResponseEntity.ok(questionMCQOutDto);
    }

    // TODO: more update methods must be here!

    // For bulk question processing via *.rtp-file; as for now only applicable to MCQ
    @PostMapping(value = "/instructor/questions-mcq-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionsParsingResultOutDto> saveMany(@RequestParam("file") MultipartFile multipartFile,
                                                @RequestParam Long themeId, @RequestParam boolean confirmed) {
        QuestionsParsingResultOutDto parsingResultOutDto =
                questionsFileParserService.parseAndSave(multipartFile, themeId,  confirmed);
        return ResponseEntity.ok(parsingResultOutDto);
    }
}

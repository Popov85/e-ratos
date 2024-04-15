package ua.edu.ratos.web.session;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.config.ControlTime;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.SessionDataMap;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.out.HelpMinOutDto;
import ua.edu.ratos.service.dto.session.ComplaintInDto;
import ua.edu.ratos.service.dto.session.ResultPerQuestionOutDto;
import ua.edu.ratos.service.dto.session.StarredInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.session.EducationalSessionService;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = {"/student/session", "/lms/session"})
@RequiredArgsConstructor
public class EducationalSessionController {

    private final EducationalSessionService educationalSessionService;

    //-----------------------------------------------PRESERVE/RETRIEVE--------------------------------------------------
    @ControlTime
    @GetMapping(value = "/preserve/{schemeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> preserve(@PathVariable Long schemeId,
                                        @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                                        HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        String sessionId = session.getId();
        String key = educationalSessionService.preserve(sessionId, sessionData);
        sessionDataMap.remove(schemeId);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Preserved session = {}", key);
        return Collections.singletonMap("key", key);
    }

    @GetMapping(value = "/retrieve/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchOutDto> retrieve(@PathVariable String key, HttpSession session) {
        Object sessionDataAttribute = session.getAttribute("sessionDataMap");
        SessionDataMap sessionDataMap = ((sessionDataAttribute == null)
                ? new SessionDataMap() : (SessionDataMap) sessionDataAttribute);
        BatchOutDto batchOutDto = educationalSessionService.retrieve(key, sessionDataMap);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Restored session = {}", key);
        return ResponseEntity.ok(batchOutDto);
    }

    @ControlTime
    @GetMapping(value = "/pause/{schemeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void pause(@PathVariable Long schemeId,
                      @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                      HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        educationalSessionService.pause(sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Paused session = {}, schemeId = {}", sessionData, schemeId);
    }

    @GetMapping(value = "/proceed/{schemeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void proceed(@PathVariable Long schemeId,
                        @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                        HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        educationalSessionService.proceed(sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Un-paused session = {}, schemeId = {}", sessionData, schemeId);
    }

    //---------------------------------------------------MORE-----------------------------------------------------------
    @PostMapping(value = "/check/{schemeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultPerQuestionOutDto> check(@PathVariable Long schemeId,
                                                         @RequestBody Response response,
                                                         @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        ResultPerQuestionOutDto result = educationalSessionService.check(response, sessionData);
        log.debug("Checked response = {} with score = {}", response, result.getScore());
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/check/{schemeId}/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultPerQuestionOutDto> check(@PathVariable Long schemeId,
                                                         @PathVariable Long questionId,
                                                         @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        ResultPerQuestionOutDto result = educationalSessionService.shows(questionId, sessionData);
        log.debug("Provided the correct answer to the questionId = {}", questionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/schemes/{schemeId}/questions/{questionId}/helped", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HelpMinOutDto> getHelp(@PathVariable Long schemeId,
                                                 @PathVariable Long questionId,
                                                 @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                                                 HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        HelpMinOutDto help = educationalSessionService.help(questionId, sessionData);
        sessionDataMap.replace(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Retrieved Help for questionId = {}, help = {}", questionId, help);
        return ResponseEntity.ok(help);
    }

    @PostMapping(value = "/schemes/{schemeId}/questions/{questionId}/starred", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setStarred(@PathVariable Long schemeId,
                           @PathVariable Long questionId,
                           @Valid @RequestBody StarredInDto dto,
                           @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                           HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        educationalSessionService.star(dto, sessionData);
        sessionDataMap.replace(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Starred questionId = {}, stars = {}", questionId, dto.getStars());
    }

    @DeleteMapping(value = "/schemes/{schemeId}/questions/{questionId}/starred")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void unStarred(@PathVariable Long schemeId,
                          @PathVariable Long questionId,
                          @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                          HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        educationalSessionService.unStar(questionId, sessionData);
        sessionDataMap.replace(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("UnStarred questionId = {}", questionId);
    }

    @PutMapping(value = "/schemes/{schemeId}/questions/{questionId}/skipped")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setSkipped(@PathVariable Long schemeId,
                           @PathVariable Long questionId,
                           @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                           HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        educationalSessionService.skip(questionId, sessionData);
        sessionDataMap.replace(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Skipped questionId = {}", questionId);
    }

    @PostMapping(value = "/schemes/{schemeId}/questions/{questionId}/complained", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setComplaint(@PathVariable Long schemeId,
                             @PathVariable Long questionId,
                             @Valid @RequestBody ComplaintInDto dto,
                             @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                             HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        educationalSessionService.complain(dto, sessionData);
        sessionDataMap.replace(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Complained about questionId = {}, complaint = {}", questionId, dto);
    }
}

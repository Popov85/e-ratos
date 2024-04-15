package ua.edu.ratos.web.session;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.config.ControlTime;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.SessionDataMap;
import ua.edu.ratos.service.dto.session.ResultOutDto;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.session.GenericSessionService;

@Slf4j
@RestController
@RequestMapping("/student/session")
@RequiredArgsConstructor
public class GenericSessionController {

    private final GenericSessionService sessionService;

    @GetMapping(value = "/start", params = "schemeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchOutDto> start(@RequestParam Long schemeId, HttpSession session) {
        Object sessionDataAttribute = session.getAttribute("sessionDataMap");
        SessionDataMap sessionDataMap = ((sessionDataAttribute == null)
                ? new SessionDataMap() : (SessionDataMap) sessionDataAttribute);
        sessionDataMap.controlAndThrow(schemeId);
        final SessionData sessionData = sessionService.start(schemeId);
        sessionDataMap.add(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Started non-LMS session for a user taking schemeId = {}, map = {}", schemeId, sessionDataMap);
        return ResponseEntity.ok(sessionData.getCurrentBatch().orElseThrow(() -> new IllegalStateException("Current batch was not found!")));
    }

    @ControlTime
    @PostMapping(value = "/next", params = "schemeId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchOutDto> next(@RequestParam Long schemeId,
                                            @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                                            @RequestBody BatchInDto batchInDto,
                                            HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        BatchOutDto batchOut = sessionService.next(batchInDto, sessionData);
        // Update sessionData in the store!
        sessionDataMap.replace(schemeId, sessionData);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Next batch in learning session = {}", batchOut);
        return ResponseEntity.ok(batchOut);
    }

    @ControlTime
    @GetMapping(value = "/current", params = "schemeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchOutDto> current(@RequestParam Long schemeId, @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        return ResponseEntity.ok(sessionService.current(sessionData));
    }

    @ControlTime
    @PostMapping(value = "/finish-batch", params = "schemeId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultOutDto> finish(@RequestParam Long schemeId, @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                                               @RequestBody BatchInDto batchInDto,
                                               HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        final ResultOutDto resultOut = sessionService.finish(batchInDto, sessionData);
        sessionDataMap.remove(schemeId);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Finished learning session with last batch evaluating, result = {}", resultOut);
        return ResponseEntity.ok(resultOut);
    }

    @ControlTime
    @GetMapping(value = "/finish", params = "schemeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultOutDto> finish(@RequestParam Long schemeId,
                                               @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                                               HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        final ResultOutDto resultOut = sessionService.finish(sessionData);
        sessionDataMap.remove(schemeId);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Finished learning session without last batch evaluating");
        return ResponseEntity.ok(resultOut);
    }

    @ControlTime
    @GetMapping(value = "/cancel", params = "schemeId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultOutDto> cancel(@RequestParam Long schemeId,
                                               @SessionAttribute("sessionDataMap") SessionDataMap sessionDataMap,
                                               HttpSession session) {
        SessionData sessionData = sessionDataMap.getOrElseThrow(schemeId);
        final ResultOutDto resultOut = sessionService.cancel(sessionData);
        sessionDataMap.remove(schemeId);
        session.setAttribute("sessionDataMap", sessionDataMap);
        log.debug("Cancelled learning session, {}", resultOut);
        return ResponseEntity.ok(resultOut);
    }
}

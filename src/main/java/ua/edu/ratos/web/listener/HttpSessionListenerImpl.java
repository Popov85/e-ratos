package ua.edu.ratos.web.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.service.domain.SessionDataMap;
import ua.edu.ratos.service.session.GenericSessionService;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/**
 * Too long inactivity case.
 * Default authentication session timeout 200 min.
 * (User takes this time to answer up to 200 questions in a single batch,
 * possibly with no interaction with server).
 * Deal with session data being about to be lost.
 * We presume that business time-out was already fired for this session.
 * It doesn't work for jdbc backed http sessions.
 *
 * @see org.springframework.session.jdbc.JdbcOperationsSessionRepository
 */
@Slf4j
@Component
@AllArgsConstructor
public class HttpSessionListenerImpl implements HttpSessionListener {

    private final GenericSessionService genericSessionService;

    private final AppProperties appProperties;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        log.trace("Http session created, sessionId = {}", event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        log.debug("HttpSession timeout, long inactivity case");
        HttpSession session = event.getSession();
        Object sessionDataAttribute = session.getAttribute("sessionDataMap");
        SessionDataMap sessionDataMap = ((sessionDataAttribute == null)
                ? new SessionDataMap() : (SessionDataMap) sessionDataAttribute);
        if (sessionDataMap.getOpenedSessions().size() > 0) {
            if (appProperties.getSession().isSaveAbandonedResults()) {
                log.debug("HttpSession timeout, long inactivity case, try to save abandoned results to DB");
                // we presume that time dedicated to this session already gone,
                // then time dedicated to the whole security session was gone all the more so!
                try {
                    sessionDataMap.getOpenedSessions().values()
                            .forEach(genericSessionService::finish);
                } catch (Exception e) {
                    log.error("Failed to save an abandoned session(s), message = {}", e.getMessage());
                }
            } else {
                log.debug("SessionData(s) will be forever lost...");
            }
        } else {
            log.debug("No sessionData(s) are kept in this http session, id = {}, just ignore...", session.getId());
        }
    }
}

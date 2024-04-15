package ua.edu.ratos.service.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.web.exception.SessionAlreadyOpenedException;
import ua.edu.ratos.web.exception.SessionNotFoundException;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to hold multiple opened learning sessions.
 * Consider such scenario:
 * 1) User started one scheme, not finished it;
 * 2) Switched to another scheme and started it simultaneously;
 * 3) Then remembered to finish the previous one, returned to it and finished;
 * All within single http session scope.
 */
@Slf4j
@Getter
public class SessionDataMap implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Key - schemeId;
     * Value - corresponding SessionData object
     */
    private final Map<Long, SessionData> openedSessions = new HashMap<>();

    /**
     * Checks if the requested schemeId is already present in the sessionDataMap;
     * If present, the current http session already has an opened session for the requested scheme;
     * The client should propose to return back to the opened session or close it.
     * @param schemeId requested schemeId
     */
    public void controlAndThrow(@NonNull final Long schemeId) {
        if (openedSessions.containsKey(schemeId))
            throw new SessionAlreadyOpenedException(schemeId);
        log.debug("Check OK, sessionData with given schemeId = {} is absent, proceed", schemeId);
    }

    public SessionData getOrElseThrow(@NonNull final Long schemeId) {
        SessionData sessionData = openedSessions.get(schemeId);
        if (sessionData==null) throw new SessionNotFoundException(schemeId);
        log.debug("Retrieved sessionData in the map for schemeId = {}", schemeId);
        return sessionData;
    }

    public void add(@NonNull final Long schemeId, @NonNull final SessionData sessionData) {
        controlAndThrow(schemeId);
        openedSessions.put(schemeId, sessionData);
        log.debug("Added sessionData to the map for schemeId = {}, total schemeIds in map = {}",
                schemeId, getOpenedSessions().size());
    }

    public void replace(@NonNull final Long schemeId, @NonNull final SessionData sessionData) {
        openedSessions.put(schemeId, sessionData);
        log.debug("Replaced sessionData to the map for schemeId = {}, total schemeIds in map = {}",
                schemeId, getOpenedSessions().size());
    }

    public void remove(@NonNull final Long schemeId) {
        SessionData remove = openedSessions.remove(schemeId);
        if (remove==null) {
            log.warn("Requested key was not found in the map");
        } else {
            log.debug("Removed sessionData from the map for schemeId = {} total schemeIds in map = {}",
                    schemeId, getOpenedSessions().size());
        }
    }

}

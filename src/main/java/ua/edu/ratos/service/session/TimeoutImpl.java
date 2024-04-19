package ua.edu.ratos.service.session;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.web.exception.RunOutOfTimeException;

import java.time.LocalDateTime;

/**
 * This class aims to keep timeout info throughout the request.
 * Set timeout info as early as possible in request flow and then use getters to check if batch or the whole session was timeouted.
 * A separate class is needed to eliminate borderline cases when multiple checks are needed in different places (methods)
 * of the current thread stack. Thus, we always get a consistent (across request) value for boolean timeouts.
 */
@Slf4j
@Getter
@Setter
@ToString
@Component
@RequestScope
@Profile({"prod", "stage", "dev"})
public class TimeoutImpl implements Timeout {

    private final AppProperties appProperties;

    @Autowired
    public TimeoutImpl(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    private boolean isSessionTimeout;

    private boolean isBatchTimeout;

    // Flag indicating that state has already been set within this request scope
    private boolean isSet;

    @Override
    public void setTimeout(@NonNull final SessionData sessionData) {
        if (isSet) throw
                new IllegalStateException("Timeout state is already set: state sets only once at the start of request flow");
        this.isSessionTimeout = isTimeoutedWithLeeway(sessionData.getSessionTimeout());
        this.isBatchTimeout = isTimeoutedWithLeeway(sessionData.getCurrentBatchTimeout());
        this.isSet = true;
    }

    @Override
    public void controlSessionTimeout() {
        if (!isSet) throw new IllegalStateException("Timeout state is not set yet!");
        if (isSessionTimeout) throw new RunOutOfTimeException();
    }

    @Override
    public boolean isTimeouted() {
        if (!isSet) throw new IllegalStateException("Timeout state is not set yet!");
        return (isSessionTimeout || isBatchTimeout);
    }

    private boolean isTimeoutedWithLeeway(LocalDateTime businessTimeout) {
        if (TimingService.isUnlimited(businessTimeout)) return false;
        // Add timeout leeway if any (non zero)
        long leeway = appProperties.getSession().getTimeoutLeeway();
        return LocalDateTime.now().isAfter(businessTimeout.plusSeconds(leeway));
    }
}

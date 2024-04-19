package ua.edu.ratos.service.session;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ua.edu.ratos.service.domain.SessionData;

@Component
@Profile({"test"})
public class TimeoutTestImpl implements ua.edu.ratos.service.session.Timeout {

    @Override
    public void controlSessionTimeout() {
        return;
    }

    @Override
    public boolean isTimeouted() {
        return false;
    }

    @Override
    public boolean isSessionTimeout() {
        return false;
    }

    @Override
    public boolean isBatchTimeout() {
        return false;
    }

    @Override
    public void setTimeout(SessionData s) {
        return;
    }
}

package ua.edu.ratos.security.lti;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.oauth.provider.ConsumerCredentials;

import java.security.Principal;
import java.util.Optional;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LTIToolConsumerCredentials extends ConsumerCredentials implements Principal {

    private final Long lmsId;

    protected LTIToolConsumerCredentials(Long lmsId, String consumerKey, String signature, String signatureMethod, String signatureBaseString, String token) {
        super(consumerKey, signature, signatureMethod, signatureBaseString, token);
        this.lmsId = lmsId;
    }

    public static LTIToolConsumerCredentials create(@NonNull final Long lmsId, @NonNull final ConsumerCredentials consumerCredentials) {
        return new LTIToolConsumerCredentials(lmsId,
                consumerCredentials.getConsumerKey(),
                consumerCredentials.getSignature(),
                consumerCredentials.getSignatureMethod(),
                consumerCredentials.getSignatureBaseString(),
                consumerCredentials.getToken());
    }

    private String email;

    private LISUser user;

    private LTIOutcomeParams outcome;

    public ConsumerCredentials getConsumerCredentials() {
        return new ConsumerCredentials(this.getConsumerKey(), this.getSignature(), this.getSignatureMethod(), this.getSignatureBaseString(), this.getToken());
    }

    /**
     * Recommended parameter to include to a launch request to smoothly recognize a learner;
     * As per LTI v 1.1.1 specification the request parameter is called: "lis_person_contact_email_primary"
     * @see <a href="http://www.imsglobal.org/specs/ltiv1p1p1/implementation-guide">LTI v 1.1.1</a>
     */
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    /**
     * Most often TC (LMS) do not send these parameters for safety reasons
     */
    public Optional<LISUser> getUser() {
        return Optional.ofNullable(user);
    }

    /**
     * Must be present if TC (LMS) wants TP to send outcome grades back;
     */
    public Optional<LTIOutcomeParams> getOutcome() {
        return Optional.ofNullable(outcome);
    }

    /**
     * Unique identifier of a recognized TC (LMS) based on the pair key-secret;
     */
    @Override
    public String getName() {
        return lmsId.toString();
    }

}

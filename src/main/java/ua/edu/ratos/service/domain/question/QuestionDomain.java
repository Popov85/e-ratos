package ua.edu.ratos.service.domain.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.edu.ratos.service.domain.HelpDomain;
import ua.edu.ratos.service.domain.ResourceDomain;
import ua.edu.ratos.service.domain.ThemeDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This domain object fully represents Question with correct answers for evaluating purposes
 * But this version is JSON-serializable and can be stored in DB as text within SessionData
 */
@Setter
@Getter
@ToString(callSuper = true, exclude = {"themeDomain", "helpDomain", "resourceDomains"})
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
public abstract class QuestionDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long questionId;

    // Number of this question in the individual sequence
    protected int serialNumber;

    protected String question;

    protected byte level;

    protected long type;

    protected ThemeDomain themeDomain;

    protected boolean partialResponseAllowed;

    @JsonProperty("helpDomain")
    protected HelpDomain helpDomain;

    @JsonProperty("resourceDomains")
    protected Set<ResourceDomain> resourceDomains = new HashSet<>();

    protected boolean required;

    @JsonIgnore
    public Optional<HelpDomain> getHelpDomain() {
        return Optional.ofNullable(helpDomain);
    }

    @JsonIgnore
    public Optional<ResourceDomain> getResourceDomain() {
        return Optional.ofNullable(
                (resourceDomains != null && !resourceDomains.isEmpty())
                        ? resourceDomains.iterator().next()
                        : null);
    }

    /**
     * For educational sessions, to be able to show right answers
     * @return DTO correct answer object
     */
    public abstract Object getCorrectAnswer();

    /**
     * Transforms domain object to DTO object for learning session.
     * DTO object is different from domain object in that is hides correct answers as well as help
     * @return DTO question object for learning session
     */
    public abstract QuestionSessionOutDto toDto();


    /**
     * Evaluates this question based on the Response object provided
     * @param response a corresponding Response object
     * @return value from [0; 100]
     */
    public abstract double evaluate(Response response);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionDomain questionDomain = (QuestionDomain) o;
        return Objects.equals(questionId, questionDomain.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }
}

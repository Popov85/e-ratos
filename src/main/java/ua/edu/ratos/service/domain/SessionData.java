package ua.edu.ratos.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.session.deserializer.SessionDataDeserializer;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SessionData domain mutable object that holds all the info regarding the state of current learning session.
 * Created when new learning session start is requested,
 * Destroyed when learning session is cancelled or finished,
 * Serialized when learning session is requested to be preserved in a DB for unlimited time;
 * Mutable as the learning session proceeds.
 * Here are 3 scenarios:
 * <ul>
 * <li>Scenario 0: Normal save<br>
 * 1) Result is stored in database
 * 2) User gets result
 * 3) Key is removed from memory (auth. session) programmatically
 * </li>
 * <li>Scenario 1: User is inactive for longer than is set by session settings (business time limit)<br>
 * 1) Data still alive in memory for 12 hours.
 * 2) Next time user requests to continue within TTL time (12 hours), the result is returned.
 * 3) Result is stored in database, with flag expired
 * 4) Key is removed from memory (auth) programmatically
 * </li>
 * <li>Scenario 2: User is inactive for more than 12 hours.<br>
 * 1) SessionData data for this key is forever lost in memory due to timeout.
 * 2) User gets his current result if present in incoming BatchOutDto object.
 * 3) Result is not stored in database.
 * </li>
 * </ul>
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonDeserialize(using = SessionDataDeserializer.class)
public class SessionData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String BUILD_ERROR = "Failed to create SessionData: wrong object state";

    private final Long userId;

    private final SchemeDomain schemeDomain;

    private final List<QuestionDomain> sequence;

    @JsonIgnore
    private final Map<Long, QuestionDomain> questionsMap;

    // ---Constructors are private, use static factory methods instead----

    private SessionData(Long lmsId, Long userId, SchemeDomain schemeDomain, List<QuestionDomain> sequence) {
        this(userId, schemeDomain, sequence);
        this.lmsId = lmsId;
    }

    private SessionData(Long userId, SchemeDomain schemeDomain, List<QuestionDomain> sequence) {
        this.userId = userId;
        this.schemeDomain = schemeDomain;
        if (sequence.isEmpty())
            throw new IllegalStateException(BUILD_ERROR);
        this.sequence = sequence;
        // Only for look-up purposes
        this.questionsMap = sequence.stream().collect(Collectors.toMap(q -> q.getQuestionId(), q -> q));

        SettingsDomain s = schemeDomain.getSettingsDomain();
        int secondsPerQuestion = s.getSecondsPerQuestion();
        short questionsPerSheet = s.getQuestionsPerSheet();
        boolean strictControlTimePerQuestion = s.isStrictControlTimePerQuestion();

        if (questionsPerSheet > sequence.size())
            throw new IllegalStateException(BUILD_ERROR);

        boolean isTimeLimited = secondsPerQuestion>0;

        this.sessionTimeout = isTimeLimited ?
                LocalDateTime.now().plusSeconds(sequence.size() * secondsPerQuestion): LocalDateTime.MAX;
        this.currentBatchTimeout = isTimeLimited && strictControlTimePerQuestion ?
                LocalDateTime.now().plusSeconds(secondsPerQuestion * questionsPerSheet) : LocalDateTime.MAX;

        if (!this.sessionTimeout.isEqual(LocalDateTime.MAX)
                && !this.currentBatchTimeout.isEqual(LocalDateTime.MAX)
                && this.sessionTimeout.isBefore(this.currentBatchTimeout))
            throw new IllegalStateException(BUILD_ERROR);
    }

    // --- Factory methods for constructing the instances ---

    public static SessionData createNoLMS(@NonNull final Long userId,
                                          @NonNull final SchemeDomain schemeDomain,
                                          @NonNull final List<QuestionDomain> sequence) {
        return new SessionData(userId, schemeDomain, sequence);
    }

    public static SessionData createFromLMS(@NonNull final Long lmsId,
                                            @NonNull final Long userId,
                                            @NonNull final SchemeDomain schemeDomain,
                                            @NonNull final List<QuestionDomain> sequence) {
        return new SessionData(lmsId, userId, schemeDomain, sequence);
    }

    /**
     * Optional value for those session that were created from within an LMS environment
     */
    private Long lmsId;

    /**
     * Timeouts are not final, as with suspensions these values will change!
     */
    private LocalDateTime sessionTimeout;

    /**
     * Current BatchOutDto timeout, if a response is timeouted,
     * all the questions in the batch are penalised according to settings;
     */
    private LocalDateTime currentBatchTimeout;

    /**
     * Current question index in array, index starting from which we provide questions for the next batch;
     * [currentIndex; currentIndex+batchSize)
     * E.g., currentIndex + batchSize, if currentIndex = 0 and batchSize = 5, currentIndex = 0+5 = 5
     */
    private int currentIndex = 0;

    /**
     * Current BatchOutDto, convenience state for BatchInDto completeness comparison:
     * Whether all the provided questions from BatchOutDto were found in BatchInDto?
     * Also used to return current batch by API methods.
     */
    private BatchOutDto currentBatch = null;

    /**
     * When the current batch was created and sent to user
     * (for tracking time spend on a batch)
     */
    private LocalDateTime currentBatchIssued = LocalDateTime.now();

    /**
     * Data holder for tracking the current progress;
     * Init with default values
     */
    private ProgressData progressData = new ProgressData();

    /**
     * Metadata about a Question, born in the interaction between a user;
     * Key is the question's ID
     */
    private Map<Long, MetaData> metaData = new HashMap<>();

    /**
     * If this session has been suspended (paused/preserved);
     */
    private boolean suspended;


    @JsonProperty("currentBatch")
    public BatchOutDto getCurrentBatchOrNull() {
        return getCurrentBatch().orElse(null);
    }

    @JsonProperty("lmsId")
    public Long getLMSIdOrNull() {
        return getLmsId().orElse(null);
    }

    @JsonIgnore
    public Optional<Long> getLmsId() {
        return Optional.ofNullable(this.lmsId);
    }

    @JsonIgnore
    public Optional<BatchOutDto> getCurrentBatch() {
        return Optional.ofNullable(this.currentBatch);
    }

    @JsonIgnore
    public boolean isSessionTimeLimited() {
        return !sessionTimeout.isEqual(LocalDateTime.MAX);
    }

    @JsonIgnore
    public boolean isBatchTimeLimited() {
        return !currentBatchTimeout.isEqual(LocalDateTime.MAX);
    }

    @JsonIgnore
    public boolean hasMoreQuestions() {
        return (currentIndex < sequence.size());
    }

    @JsonIgnore
    public boolean isDynamicSession() {
        ModeDomain mode = this.schemeDomain.getModeDomain();
        return (mode.isSkipable() || mode.isPyramid());
    }

    @JsonIgnore
    public boolean isGameableSession() {
        ModeDomain mode = this.schemeDomain.getModeDomain();
        return (!mode.isSkipable() && !mode.isPyramid() && !mode.isRightAnswer());
    }

    @JsonIgnore
    public boolean isSingleBatchSession() {
        return this.schemeDomain.getSettingsDomain().getQuestionsPerSheet() == this.sequence.size();
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "userId=" + userId +
                ", schemeId=" + schemeDomain.getSchemeId() +
                ", lmsId=" + lmsId +
                ", sequence size=" + sequence.size() +
                ", sessionTimeout=" + sessionTimeout +
                ", currentBatchTimeout=" + currentBatchTimeout +
                ", currentIndex=" + currentIndex +
                ", suspended=" + suspended +
                '}';
    }
}

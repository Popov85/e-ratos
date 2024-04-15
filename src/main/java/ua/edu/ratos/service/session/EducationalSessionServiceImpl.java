package ua.edu.ratos.service.session;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.dao.entity.Scheme;
import ua.edu.ratos.dao.entity.SessionPreserved;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.ComplaintService;
import ua.edu.ratos.service.SessionPreservedService;
import ua.edu.ratos.service.domain.HelpDomain;
import ua.edu.ratos.service.domain.ModeDomain;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.SessionDataMap;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.out.HelpMinOutDto;
import ua.edu.ratos.service.dto.session.ComplaintInDto;
import ua.edu.ratos.service.dto.session.ResultPerQuestionOutDto;
import ua.edu.ratos.service.dto.session.StarredInDto;
import ua.edu.ratos.service.dto.session.batch.BatchOutDto;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;
import ua.edu.ratos.service.transformer.mapper.HelpMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EducationalSessionServiceImpl implements EducationalSessionService {

    private static final String NO_SUCH_QUESTION = "No such questionId found in the current batch, questionId = ";
    private static final String OPERATION_NOT_ALLOWED = "This operation is not allowed by session settings";
    private static final String PRESERVED_SESSIONS_LIMIT = "You have reached the limit of preserved sessions";
    private static final String NO_HELP_AVAILABLE = "No help is available for this question";
    private static final String STARRED_QUESTIONS_LIMIT = "You have reached the limit of starred questions, remove some to proceed";

    @PersistenceContext
    private final EntityManager em;

    private final AppProperties appProperties;

    private final ShiftService shiftService;

    private final MetaDataService metaDataService;

    private final UserQuestionStarredService userQuestionStarredService;

    private final SessionPreservedService sessionPreservedService;

    private final HelpMapper helpMapper;

    private final ComplaintService complaintService;

    private final SecurityUtils securityUtils;

    //--------------------------------------------------PRESERVE--------------------------------------------------------

    @Override
    @Transactional
    public String preserve(@NonNull final String key, @NonNull final SessionData sessionData) {
        ModeDomain modeDomain = sessionData.getSchemeDomain().getModeDomain();
        if (!modeDomain.isPreservable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        Long requestingUserId = securityUtils.getAuthUserId();
        long preservedExist = sessionPreservedService.countByUserId(requestingUserId);
        int preservedLimit = appProperties.getSession().getPreservedLimit();
        if (preservedExist >= preservedLimit) throw new UnsupportedOperationException(PRESERVED_SESSIONS_LIMIT);
        // If session is paused, implicitly continue to keep timings up to date
        if (sessionData.isSuspended()) TimingService.updateTimingsWhenContinued(sessionData);
        return sessionPreservedService.save(key, sessionData);
    }

    @Override
    @Transactional
    public BatchOutDto retrieve(@NonNull final String key, @NonNull final SessionDataMap sessionDataMap) {
        SessionPreserved sessionPreserved = sessionPreservedService.findOneById(key);
        Long schemeId = sessionPreserved.getScheme().getSchemeId();
        sessionDataMap.controlAndThrow(schemeId);
        SessionData sessionData = sessionPreservedService.retrieve(sessionPreserved);
        BatchOutDto batchOutDto = sessionData.getCurrentBatch()
                .orElseThrow(() -> new RuntimeException("Current batch not found!"));
        sessionDataMap.add(schemeId, sessionData);
        return batchOutDto;
    }

    //-----------------------------------------------PAUSE--------------------------------------------------------------

    @Override
    public void pause(@NonNull final SessionData sessionData) {
        ModeDomain modeDomain = sessionData.getSchemeDomain().getModeDomain();
        if (!modeDomain.isPauseable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        if (sessionData.isSuspended()) throw new IllegalStateException("Session is already suspended");
        TimingService.updateTimingsWhenPaused(sessionData);
    }

    @Override
    public void proceed(@NonNull final SessionData sessionData) {
        ModeDomain modeDomain = sessionData.getSchemeDomain().getModeDomain();
        if (!modeDomain.isPauseable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        if (!sessionData.isSuspended()) throw new IllegalStateException("Session is not suspended!");
        TimingService.updateTimingsWhenContinued(sessionData);
    }

    //-----------------------------------------------CHECK--------------------------------------------------------------

    @Override
    public ResultPerQuestionOutDto check(@NonNull final Response response, @NonNull final SessionData sessionData) {
        Long questionId = response.getQuestionId();
        ModeDomain modeDomain = validateRequest(questionId, sessionData);
        if (!modeDomain.isRightAnswer()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        if (sessionData.isSuspended()) throw new IllegalStateException("Session is suspended!");
        QuestionDomain questionDomain = sessionData.getQuestionsMap().get(questionId);
        double score = (!response.isNullable() ? questionDomain.evaluate(response) : 0);
        return new ResultPerQuestionOutDto()
                .setQuestion(questionDomain.toDto())
                .setScore(score)
                .setResponse(response)
                .setCorrectAnswer(questionDomain.getCorrectAnswer());
    }

    @Override
    public ResultPerQuestionOutDto shows(@NonNull final Long questionId, @NonNull final SessionData sessionData) {
        ModeDomain modeDomain = validateRequest(questionId, sessionData);
        if (!modeDomain.isRightAnswer()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        if (sessionData.isSuspended()) throw new IllegalStateException("Session is suspended!");
        QuestionDomain questionDomain = sessionData.getQuestionsMap().get(questionId);
        return new ResultPerQuestionOutDto()// score by default = 0
                .setQuestion(questionDomain.toDto())
                .setCorrectAnswer(questionDomain.getCorrectAnswer());
    }

    //-----------------------------------------------------AJAX---------------------------------------------------------

    @Override
    @Transactional
    public void star(@NonNull final StarredInDto starred, @NonNull final SessionData sessionData) {
        ModeDomain modeDomain = validateRequest(starred.getQuestionId(), sessionData);
        if (!modeDomain.isStarrable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        int starredLimit = appProperties.getSession().getStarredLimit();
        if (userQuestionStarredService.countByUserId()>=starredLimit)  throw new UnsupportedOperationException(STARRED_QUESTIONS_LIMIT);
        userQuestionStarredService.save(starred.getQuestionId(), starred.getStars());
    }

    @Override
    @Transactional
    public void unStar(@NonNull final Long questionId, @NonNull final SessionData sessionData) {
        ModeDomain modeDomain = validateRequest(questionId, sessionData);
        if (!modeDomain.isStarrable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        userQuestionStarredService.deleteById(questionId);
    }

    @Override
    @Transactional
    public void complain(@NonNull final ComplaintInDto complaint, @NonNull final SessionData sessionData) {
        Long questionId = complaint.getQuestionId();
        ModeDomain modeDomain = validateRequest(questionId, sessionData);
        if (!modeDomain.isReportable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        Long schemeId = sessionData.getSchemeDomain().getSchemeId();
        Scheme scheme = em.find(Scheme.class, schemeId);
        Long depId = scheme.getDepartment().getDepId();
        complaintService.save(questionId, complaint.getComplaintTypeIds(), depId);
    }

    @Override
    public HelpMinOutDto help(@NonNull final Long questionId, @NonNull final SessionData sessionData) {
        ModeDomain modeDomain = validateRequest(questionId, sessionData);
        if (!modeDomain.isHelpable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        Optional<HelpDomain> help = sessionData.getQuestionsMap().get(questionId).getHelpDomain();
        if (!help.isPresent()) throw new IllegalStateException(NO_HELP_AVAILABLE);
        metaDataService.createOrUpdateHelp(sessionData, questionId);
        return helpMapper.toDto(help.get());
    }

    @Override
    public void skip(@NonNull final Long questionId, @NonNull final SessionData sessionData) {
        ModeDomain modeDomain = validateRequest(questionId, sessionData);
        if (!modeDomain.isSkipable()) throw new UnsupportedOperationException(OPERATION_NOT_ALLOWED);
        QuestionSessionOutDto questionToRemove = sessionData.getCurrentBatch().get().getBatchMap().get(questionId);
        sessionData.getCurrentBatch().get().getQuestions().remove(questionToRemove);
        shiftService.doShift(questionId, sessionData);
        metaDataService.createOrUpdateSkip(sessionData, questionId);
    }

    private ModeDomain validateRequest(@NonNull final Long questionId, @NonNull final SessionData sessionData) {
        QuestionSessionOutDto question = sessionData.getCurrentBatch().get().getBatchMap().get(questionId);
        if (question==null) throw new IllegalStateException(NO_SUCH_QUESTION+questionId);
        if (sessionData.isSuspended()) throw new IllegalStateException("Session is suspended!");
        return sessionData.getSchemeDomain().getModeDomain();
    }

}

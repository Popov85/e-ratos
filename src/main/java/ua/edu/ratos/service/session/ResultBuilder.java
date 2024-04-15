package ua.edu.ratos.service.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.dao.entity.User;
import ua.edu.ratos.dao.repository.UserRepository;
import ua.edu.ratos.service.domain.*;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.session.grade.GradedResult;
import ua.edu.ratos.service.transformer.mapper.UserMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Service
@AllArgsConstructor
public class ResultBuilder {

    private final AppProperties appProperties;

    private final GradingService gradingService;

    private final ProgressDataService progressDataService;

    private final GameService gameService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final EvaluatorPostProcessor evaluatorPostProcessor;

    /**
     * Builds the result object
     * It is only invoked at the valid end of a learning session.
     *
     * @param sessionData sessionData
     * @param timeOuted   is the learning session has been timeouted
     * @param cancelled   is the learning session has been cancelled
     * @return ResultDomain domain obj that represents the result on current session
     */
    @Transactional
    public ResultDomain build(@NonNull final SessionData sessionData, boolean timeOuted, boolean cancelled) {
        // 1. Calculate percent (based on actual score (+bounty, - penalty))
        double percent = progressDataService.getCurrentActualScore(sessionData);
        // Normalize the final percent to 100.
        if (percent > 100) percent = 100;
        // 2. Calculate grade and if passed verdict
        final SchemeDomain schemeDomain = sessionData.getSchemeDomain();
        final GradingDomain gradingDomain = schemeDomain.getGradingDomain();
        final GradedResult gradedResult = gradingService.grade(schemeDomain.getSchemeId(), gradingDomain, percent);

        // 3. User info
        Long userId = sessionData.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        UserDomain userDomain = userMapper.toDomain(user);

        // 4. Additional params
        Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();
        List<ResponseEvaluated> responsesEvaluated = progressDataService.toResponseEvaluated(sessionData);

        ResultDomain result = new ResultDomain()
                .setUser(userDomain)
                .setScheme(schemeDomain)
                .setPercent(percent)
                .setPassed(gradedResult.isPassed())
                .setGrade(gradedResult.getGrade())
                .setTimeOuted(timeOuted)
                .setCancelled(cancelled)
                .setTimeSpent(sessionData.getProgressData().getTimeSpent())
                .setThemeResults(getResultPerTheme(questionsMap, responsesEvaluated, schemeDomain.getSettingsDomain()))
                .setQuestionsMap(questionsMap)
                .setResponsesEvaluated(responsesEvaluated)
                .setLmsId(sessionData.getLmsId().orElse(null));

        // Gamification points (if any) according to the settings.
        // It is needed for end-user after session results inside DTO object
        // without actually waiting for completion of full long DB operations.
        AppProperties.Game props = appProperties.getGame();
        if (props.isGameOn()&&sessionData.isGameableSession()) {
            if ((!cancelled && !timeOuted)
                    || (cancelled && props.isProcessCancelledResults())
                    || (timeOuted && props.isProcessTimeoutedResults())) {
                result.setPoints(gameService.getPoints(sessionData, percent).orElse(null));
            }
        }
        return result;
    }

    /**
     * It's 2-steps process: group responses (including nullable) by theme
     * and create a list of results per theme
     *
     * @param questionsMap
     * @param responsesEvaluated
     * @return list of results per each theme within scheme
     */
    private List<ResultPerTheme> getResultPerTheme(@NonNull final Map<Long, QuestionDomain> questionsMap,
                                                   @NonNull final List<ResponseEvaluated> responsesEvaluated,
                                                   @NonNull final SettingsDomain settingsDomain) {
        // 1. Grouping by theme
        final Map<ThemeDomain, List<ResponseEvaluated>> groupedByTheme = responsesEvaluated.stream()
                .collect(Collectors.groupingBy(r -> questionsMap.get(r.getQuestionId()).getThemeDomain()));
        // 2. Calculation
        return groupedByTheme
                .entrySet()
                .stream()
                .map(e -> {
                    List<ResponseEvaluated> responses = e.getValue();
                    final int quantity = responses.size();
                    // sum scores for each theme
                    // heed level bounty and penalty!
                    final double sum = getActualScore(questionsMap, responses, settingsDomain);
                    double percent = (sum / quantity) * 100d;
                    return new ResultPerTheme(e.getKey(), quantity, percent);
                })
                .collect(Collectors.toList());
    }

    public double getActualScore(@NonNull final Map<Long, QuestionDomain> questionsMap,
                                 @NonNull final List<ResponseEvaluated> responses,
                                 @NonNull final SettingsDomain settingsDomain) {
        float l2 = settingsDomain.getLevel2Coefficient();
        float l3 = settingsDomain.getLevel3Coefficient();
        return responses
                .stream()
                .mapToDouble(r -> {
                    if (r.isPenalty()) {
                        return evaluatorPostProcessor
                                .applyPenalty(evaluatorPostProcessor
                                        .applyBounty(r.getScore(), questionsMap.get(r.getQuestionId()).getLevel(), l2, l3)) / 100d;
                    }
                    return evaluatorPostProcessor.applyBounty(r.getScore(), questionsMap.get(r.getQuestionId()).getLevel(), l2, l3) / 100d;
                })
                .sum();
    }
}

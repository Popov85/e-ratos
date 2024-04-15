package ua.edu.ratos.service.session;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.edu.ratos.service.domain.ResponseEvaluated;
import ua.edu.ratos.service.domain.SessionData;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.domain.response.Response;
import ua.edu.ratos.service.dto.session.batch.BatchInDto;
import ua.edu.ratos.service.dto.session.question.QuestionSessionOutDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BatchEvaluatorService {

    private static final String EMPTY_BATCH_OUT_EXCEPTION = "Wrong API usage: failed to evaluate BatchInDto, " +
                                                            "because of empty current BatchOutDto in SessionData";
    private final Timeout timeout;

    /**
     * Evaluates the whole non-empty BatchInDto coming from user, skipped questions (if any) are ignored (since deleted from BatchOutDto);
     * Make sure that current batch is not empty before calling this method.
     * Levels processing, response may be evaluated with a score higher than 100.
     *   In case the result is 100% correct and the corresponding coefficient is more than 1!
     *   Even if the result is not 100% correct but the corresponding coefficient is rather big, a score may appear to be higher than 100!
     * @param batchInDto batch in
     * @param sessionData SessionData object associated with the current http(s)-session
     * @return result of evaluation as a map of IDs (from BatchOutDto in SessionData) and ResponseEvaluated
     */
    public List<ResponseEvaluated> doEvaluate(@NonNull final BatchInDto batchInDto, @NonNull final SessionData sessionData) {
        //log.debug("Batch to evaluate = {}, current batch = {}", batchInDto, sessionData.getCurrentBatch());
        if (!sessionData.getCurrentBatch().isPresent() || sessionData.getCurrentBatch().get().getQuestions().isEmpty())
            throw new IllegalArgumentException(EMPTY_BATCH_OUT_EXCEPTION);

        List<ResponseEvaluated> responsesEvaluated = new ArrayList<>();

        final Map<Long, QuestionDomain> questionsMap = sessionData.getQuestionsMap();

        List<Long> toEvaluate = sessionData.getCurrentBatch()
                .orElseThrow(()->new RuntimeException("Current batch not found!")).getQuestions()
                .stream()
                .map(QuestionSessionOutDto::getQuestionId)
                .collect(Collectors.toList());

        // Try to look up responses for each question in BatchOutDto and evaluate them
        for (Long questionId : toEvaluate) {
            // look up question in sessionData
            final QuestionDomain questionDomain = questionsMap.get(questionId);
            // look up response in BatchInDto
            final Response response = batchInDto.getResponses().get(questionId);
            if (response != null) {// if found, evaluate
                double score = response.isNullable() ? 0 : questionDomain.evaluate(response);
                responsesEvaluated.add(new ResponseEvaluated(questionId, response, score, timeout.isTimeouted()));
                log.debug("Evaluated response = {}, ID = {}, score = {}", response, questionId, score);
            } else {// if not found, consider incorrect
                log.debug("Empty or no response, incorrect ID = {}", questionId);
                responsesEvaluated.add(ResponseEvaluated.buildEmpty(questionId));
            }
        }
        return responsesEvaluated;
    }

}

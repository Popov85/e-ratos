package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ua.edu.ratos.dao.entity.ResultOfStudentDetails;
import ua.edu.ratos.dao.repository.ResultOfStudentDetailsRepository;
import ua.edu.ratos.service.domain.*;
import ua.edu.ratos.service.domain.question.QuestionDomain;
import ua.edu.ratos.service.dto.out.ResultOfStudentDetailsOutDto;
import ua.edu.ratos.service.dto.session.ResultPerQuestionOutDto;
import ua.edu.ratos.service.session.EvaluatorPostProcessor;
import ua.edu.ratos.service.session.ProgressDataService;
import ua.edu.ratos.service.session.SessionDataSerializerService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultOfStudentDetailsService {

    private final ResultOfStudentDetailsRepository resultOfStudentDetailsRepository;

    private final SessionDataSerializerService serializer;

    private final ProgressDataService progressDataService;

    private final EvaluatorPostProcessor evaluatorPostProcessor;


    public ResultOfStudentDetailsOutDto getResultOfStudentDetails(@NonNull final Long resultId) {
        ResultOfStudentDetails resultOfStudentDetails = resultOfStudentDetailsRepository.findById(resultId)
                .orElseThrow(() -> new EntityNotFoundException("Result detail is not found, resultId=" + resultId));
        String jsonData = resultOfStudentDetails.getJsonData();
        SessionData sessionData = serializer.deserialize(jsonData);
        SettingsDomain sd = sessionData.getSchemeDomain().getSettingsDomain();
        Map<Long, QuestionDomain> map = sessionData.getQuestionsMap();
        List<ResponseEvaluated> responseEvaluated = progressDataService.toResponseEvaluated(sessionData);
        return new ResultOfStudentDetailsOutDto()
                .setResultId(resultId)
                .setQuestionResults(toResultsPerQuestionDto(responseEvaluated, map, sd));
    }


    private List<ResultPerQuestionOutDto> toResultsPerQuestionDto(@NonNull final List<ResponseEvaluated> responseEvaluated,
                                                                  @NonNull final Map<Long, QuestionDomain> map,
                                                                  @NonNull final SettingsDomain sd) {
        List<ResultPerQuestionOutDto> collect = responseEvaluated
                .stream()
                .map(re -> toResultPerQuestionDto(re, map.get(re.getQuestionId()), sd))
                .collect(Collectors.toList());
        return collect;
    }

    private ResultPerQuestionOutDto toResultPerQuestionDto(@NonNull final ResponseEvaluated re,
                                                           @NonNull final QuestionDomain q,
                                                           @NonNull final SettingsDomain sd) {
        Double bounty = (q.getLevel() != 1) ? evaluatorPostProcessor.getBounty(q.getLevel(), sd) : null;
        Double penalty = (re.isPenalty()) ? evaluatorPostProcessor.getPenalty() : null;

        return new ResultPerQuestionOutDto()
                .setQuestion(q.toDto())
                .setScore(re.getScore())
                .setBounty(bounty)
                .setPenalty(penalty)
                .setResponse(re.getResponse())
                .setCorrectAnswer(q.getCorrectAnswer());
    }

}

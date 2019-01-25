package ua.edu.ratos.service.domain.question;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.modelmapper.ModelMapper;
import ua.edu.ratos.service.domain.answer.AnswerSQDomain;
import ua.edu.ratos.service.dto.session.question.QuestionSQSessionOutDto;
import ua.edu.ratos.service.domain.response.ResponseSQ;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString(callSuper = true)
@Accessors(chain = true)
public class QuestionSQDomain extends QuestionDomain {
    private Set<AnswerSQDomain> answers = new HashSet<>();

    public void add(AnswerSQDomain answer) {
        this.answers.add(answer);
    }

    /**
     * We only compare list of answers (to contain all the elements and to be in the right order of elements)
     * Strict match of sequences: correct and obtained sequence must be identical, {146, 222, 281, 300, 312} = {146, 222, 281, 300, 312}
     * If at least one mis-order or mis-match - result is fully incorrect  response (0)
     * @param response
     * @return
     */
    public int evaluate(ResponseSQ response) {
        final List<Long> responseSequence = response.getAnswerIds();
        if (responseSequence==null || responseSequence.isEmpty()) return 0;
        if (responseSequence.equals(findAll())) return 100;
        return 0;
    }

    private List<Long> findAll() {
        return answers.stream()
                .sorted(Comparator.comparingInt(AnswerSQDomain::getOrder))
                .map(AnswerSQDomain::getAnswerId)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionSQSessionOutDto toDto() {
        ModelMapper modelMapper = new ModelMapper();
        QuestionSQSessionOutDto dto = modelMapper.map(this, QuestionSQSessionOutDto.class);
        dto.setAnswers(new HashSet<>());
        dto.setHelpAvailable((getHelpDomain().isPresent()) ? true : false);
        dto.setResourceDomains((getResourceDomains().isPresent()) ? getResourceDomains().get() : null);
        answers.forEach(a->dto.addAnswer(a.toDto()));
        return dto;
    }
}

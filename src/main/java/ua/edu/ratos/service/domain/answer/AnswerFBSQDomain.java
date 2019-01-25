package ua.edu.ratos.service.domain.answer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.edu.ratos.service.domain.PhraseDomain;
import ua.edu.ratos.service.domain.SettingsFBDomain;
import ua.edu.ratos.service.dto.session.answer.AnswerFBSQSessionOutDto;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class AnswerFBSQDomain {

    private Long answerId;

    private SettingsFBDomain settings;

    private Set<PhraseDomain> acceptedPhraseDomains = new HashSet<>();

    public AnswerFBSQSessionOutDto toDto() {
        return new AnswerFBSQSessionOutDto()
                .setAnswerId(answerId)
                .setSettings(settings);
    }

}

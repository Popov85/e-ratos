package ua.edu.ratos.service.dto.out.answer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.edu.ratos.service.dto.out.PhraseOutDto;
import ua.edu.ratos.service.dto.out.SettingsFBOutDto;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AnswerFBSQOutDto {

    private Long answerId;

    private SettingsFBOutDto settings;

    private Set<PhraseOutDto> acceptedPhrases;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerFBSQOutDto that = (AnswerFBSQOutDto) o;
        return Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId);
    }
}

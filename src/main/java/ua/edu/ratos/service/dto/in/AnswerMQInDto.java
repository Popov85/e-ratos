package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AnswerMQInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long answerId;

    @Positive( message = "{dto.fk.invalid}")
    private Long leftPhraseId;

    @Positive( message = "{dto.fk.invalid}")
    private Long rightPhraseId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerMQInDto that = (AnswerMQInDto) o;
        return Objects.equals(leftPhraseId, that.leftPhraseId) &&
                Objects.equals(rightPhraseId, that.rightPhraseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftPhraseId, rightPhraseId);
    }
}

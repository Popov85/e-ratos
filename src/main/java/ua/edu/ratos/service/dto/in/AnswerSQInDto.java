package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import jakarta.validation.constraints.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AnswerSQInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long answerId;

    @Positive( message = "{dto.fk.required}")
    private Long phraseId;

    @Range( min = 0, max=20, message = "{dto.range.invalid}")
    private short order;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerSQInDto that = (AnswerSQInDto) o;
        return Objects.equals(phraseId, that.phraseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phraseId);
    }
}

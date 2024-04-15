package ua.edu.ratos.service.dto.in;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import jakarta.validation.constraints.*;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AnswerMCQInDto {

    private Long answerId;

    @NotBlank( message = "{dto.string.required}")
    @Size( min = 1, max = 500, message = "{dto.string.invalid}")
    private String answer;

    @Range(min = 0, max=100, message = "{dto.range.invalid}")
    private short percent;

    public Optional<@Positive(message = "{dto.fk.optional}") Long> resourceId = Optional.empty();

    private boolean required;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerMCQInDto that = (AnswerMCQInDto) o;
        return Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }
}

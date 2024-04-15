package ua.edu.ratos.service.dto.in;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public class QuestionMCQInDto extends QuestionInDto {

    @NotEmpty(message = "{dto.collection.required}")
    @Size(min = 2, max = 10, message = "{dto.collection.invalid}")
    private Set<@Valid AnswerMCQInDto> answers;
}

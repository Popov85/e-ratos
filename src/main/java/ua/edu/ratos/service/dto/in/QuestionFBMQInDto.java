package ua.edu.ratos.service.dto.in;

import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public class QuestionFBMQInDto extends QuestionInDto {

    @NotEmpty(message = "{dto.collection.required}")
    @Size(min = 1, max = 20, message = "{dto.collection.invalid}")
    private Set<@Valid AnswerFBMQInDto> answers;
}

package ua.edu.ratos.service.dto.in;

import lombok.*;
import lombok.experimental.Accessors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

@ToString(callSuper = true)
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSQInDto extends QuestionInDto {

    @NotEmpty(message = "{dto.collection.required}")
    @Size(min = 3, max = 20, message = "{dto.collection.invalid}")
    private Set<@Valid AnswerSQInDto> answers;
}

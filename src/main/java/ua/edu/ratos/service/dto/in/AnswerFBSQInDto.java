package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AnswerFBSQInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long answerId;

    @Positive(message = "{dto.fk.required}")
    private long setId;

    @NotEmpty(message = "{dto.collection.required}")
    @Size(min = 1, max = 20, message = "{dto.collection.invalid}")
    private Set<Long> phrasesIds;
}

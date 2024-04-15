package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;


@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PhraseInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long phraseId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 1, max = 200, message = "{dto.string.invalid}")
    private String phrase;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @PositiveOrZero(message = "{dto.fk.optional}")
    private Long resourceId;
}

package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@SuppressWarnings("SpellCheckingInspection")
public class ModeInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long modeId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 50, message = "{dto.string.invalid}")
    private String name;

    private boolean helpable;

    private boolean pyramid;

    private boolean skipable;

    private boolean rightAnswer;

    private boolean preservable;

    private boolean reportable;

    private boolean starrable;
}

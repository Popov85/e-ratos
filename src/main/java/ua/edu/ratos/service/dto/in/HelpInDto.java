package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;

@ToString
@Getter
@Setter
@Accessors(chain = true)
public class HelpInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long helpId;

    @NotBlank( message = "{dto.string.required}")
    @Size(min = 1, max = 200, message = "{dto.string.invalid}")
    private String name;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 1, max = 1000, message = "{dto.string.invalid}")
    private String help;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @PositiveOrZero(message = "{dto.fk.optional}")
    private long resourceId;
}

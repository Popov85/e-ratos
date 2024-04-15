package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SettingsInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long setId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 100, message = "{dto.string.invalid}")
    private String name;

    // if 0, not specified then not limited
    @PositiveOrZero( message = "{dto.value.positiveOrZero}")
    @Max(value = 600, message = "Invalid secondsPerQuestion, max=600")
    private int secondsPerQuestion;

    @Positive(message = "{dto.value.positive}")
    @Max(value = 24, message = "Invalid questionsPerSheet, max=24")
    private short questionsPerSheet;

    @Positive(message = "I{dto.value.positive}")
    @Max(value = 365, message = "Invalid daysKeepResultDetails, max=365")
    private short daysKeepResultDetails;

    @Positive(message = "{dto.value.positive}")
    @Min(value = 1, message = "Invalid level2Coefficient, min=1")
    @Max(value = 2, message = "Invalid level2Coefficient, max=2")
    private float level2Coefficient;

    @Positive(message = "{dto.value.positive}")
    @Min(value = 1, message = "Invalid level3Coefficient, min=1")
    @Max(value = 2, message = "Invalid level3Coefficient, max=2")
    private float level3Coefficient;

}

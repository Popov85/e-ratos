package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SettingsInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long setId;

    @NotBlank(message = "Invalid name, {dto.string.required}")
    @Size(min = 2, max = 100, message = "Invalid name, {dto.string.invalid}")
    private String name;

    // if 0, not specified then not limited
    @PositiveOrZero( message = "Invalid secondsPerQuestion, {dto.value.positiveOrZero}")
    @Max(value = 600, message = "Invalid secondsPerQuestion, max=600")
    private int secondsPerQuestion;

    @Positive(message = "Invalid questionsPerSheet, {dto.value.positive}")
    @Max(value = 24, message = "Invalid questionsPerSheet, max=24")
    private short questionsPerSheet;

    @Positive(message = "Invalid daysKeepResultDetails, {dto.value.positive}")
    @Max(value = 365, message = "Invalid daysKeepResultDetails, max=365")
    private short daysKeepResultDetails;

    @Positive(message = "Invalid level2Coefficient, {dto.value.positive}")
    @Min(value = 1, message = "Invalid level2Coefficient, max=1")
    @Max(value = 2, message = "Invalid level2Coefficient, max=2")
    private float level2Coefficient;

    @Positive(message = "Invalid level3Coefficient, {dto.value.positive}")
    @Min(value = 1, message = "Invalid level3Coefficient, max=1")
    @Max(value = 2, message = "Invalid level3Coefficient, max=2")
    private float level3Coefficient;

    private boolean displayPercent;

    private boolean displayMark;

    private boolean displayThemeResults;

    private boolean strictControlTimePerQuestion;

}

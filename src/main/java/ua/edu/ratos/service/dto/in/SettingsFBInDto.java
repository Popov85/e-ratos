package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.*;

@Getter
@Setter
@ToString
public class SettingsFBInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long settingsId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 1, max = 100, message = "{dto.string.invalid}")
    private String name;

    @Min(value = 1, message = "Invalid wordsLimit, min=1")
    @Max(value = 20, message = "Invalid wordsLimit, max=20")
    private short wordsLimit;

    @Min(value = 1, message = "Invalid symbolsLimit, min=1")
    @Max(value = 200, message = "Invalid symbolsLimit, max=200")
    private short symbolsLimit;

    @Positive(message = "{dto.fk.required}")
    private long lang;

    private boolean isNumeric;

    private boolean isTypoAllowed;

    private boolean isCaseSensitive;
}

package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class OptionsInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long optId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 100, message = "{dto.string.invalid}")
    private String name;

    private boolean displayQuestionsLeft;

    private boolean displayBatchesLeft;

    private boolean displayCurrentScore;

    private boolean displayEffectiveScore;

    private boolean displayProgress;

    private boolean displayMotivationalMessages;

    private boolean displayResultScore;

    private boolean displayResultMark;

    private boolean displayTimeSpent;

    private boolean displayResultOnThemes;

    private boolean displayResultOnQuestions;
}

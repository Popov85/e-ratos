package ua.edu.ratos.service.dto.session;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@ToString
public class StarredInDto {

    @Positive(message = "{dto.fk.required}")
    private Long questionId;

    @Min(value = 1)
    @Max(value = 5)
    private byte stars;
}

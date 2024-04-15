package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.*;

@Getter
@Setter
@ToString
public class FourPointGradingInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long fourId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 100, message = "{dto.string.invalid}")
    private String name;

    @Min(value = 1)
    @Max(value = 100)
    private byte threshold3;

    @Min(value = 1)
    @Max(value = 100)
    private byte threshold4;

    @Min(value = 1)
    @Max(value = 100)
    private byte threshold5;
}

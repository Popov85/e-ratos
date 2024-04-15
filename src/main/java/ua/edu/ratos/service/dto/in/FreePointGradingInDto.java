package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
public class FreePointGradingInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long freeId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 100, message = "{dto.string.invalid}")
    private String name;

    @Min(value = 0)
    private short minValue;

    @Min(value = 0)
    private short passValue;

    @Min(value = 10000)
    private short maxValue;
}

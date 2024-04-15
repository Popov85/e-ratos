package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@ToString
public class StudentInDto {
    // For updates, solely
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long studId;

    private @Valid UserInDto user;

    @Positive(message = "{dto.fk.required}")
    private long orgId;

    @Positive(message = "{dto.fk.required}")
    private long facId;

    @Positive(message = "{dto.fk.required}")
    private long classId;

    @Min(value = 2000, message="{dto.year}")
    private int entranceYear;

}

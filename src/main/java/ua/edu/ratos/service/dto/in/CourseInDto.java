package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CourseInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long courseId;

    @NotBlank( message = "{dto.string.required}")
    @Size(min = 5, max = 300, message = "{dto.string.invalid}")
    private String name;

    @Positive(message = "{dto.fk.required}")
    private long accessId;

    private boolean active;
}

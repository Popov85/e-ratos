package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ThemeInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long themeId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 1, max = 100, message = "{dto.string.invalid}")
    private String name;

    @Positive(message = "{dto.fk.required}")
    private long courseId;

    @Positive(message = "{dto.fk.required}")
    private long accessId;
}

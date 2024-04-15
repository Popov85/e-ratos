package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.*;

@Getter
@Setter
@ToString
public class UserInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 200, message = "{dto.string.invalid}")
    private String name;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 200, message = "{dto.string.invalid}")
    private String surname;

    @Email(message = "{dto.email}")
    private String email;

    @NotEmpty(message = "{dto.string.required}")
    @Size(min = 8, max = 100, message = "{dto.string.invalid}")
    private char[] password;
}

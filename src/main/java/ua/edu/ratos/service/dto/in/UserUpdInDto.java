package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UserUpdInDto {

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

    private boolean active;
}

package ua.edu.ratos.service.dto.in;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class PasswordsInDto {

    @NotEmpty(message = "{dto.string.required}")
    @Size(min = 8, max = 100, message = "{dto.string.invalid}")
    private char[] oldPass;

    @NotEmpty(message = "{dto.string.required}")
    @Size(min = 8, max = 100, message = "{dto.string.invalid}")
    private char[] newPass;
}

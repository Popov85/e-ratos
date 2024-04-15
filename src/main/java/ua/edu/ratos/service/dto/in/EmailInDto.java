package ua.edu.ratos.service.dto.in;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.Email;

@Getter
@Setter
@ToString
public class EmailInDto {
    @Email(message = "{dto.email}")
    private String email;
}

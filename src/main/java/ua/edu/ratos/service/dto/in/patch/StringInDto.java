package ua.edu.ratos.service.dto.in.patch;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class StringInDto {

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 200, message = "{dto.string.invalid}")
    private String value;
}

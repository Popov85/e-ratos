package ua.edu.ratos.service.dto.in.patch;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UrlInDto {

    @NotBlank(message = "{dto.string.required}")

    @Size(min = 5, max = 1000, message = "{dto.string.invalid}")
    private String value;
}

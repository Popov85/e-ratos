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
public class LMSInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long lmsId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 2, max = 100, message = "{dto.string.invalid}")
    private String name;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 8, max = 1000, message = "{dto.string.invalid}")
    private String key;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 20, max = 1000, message = "{dto.string.invalid}")
    private String secret;

    @Positive(message = "{dto.fk.required}")
    private long versionId;
}

package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Optional;

@Getter
@Setter
@ToString
public class StaffInDto{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long staffId;

    private @Valid UserInDto user;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 5, max = 30, message = "{dto.string.invalid}")
    private String role;

    @NotNull(message = "dto.fk.required")
    @Positive(message = "{dto.fk.required}")
    private Long positionId;

    public Optional<@Positive(message = "{dto.fk.required}") Long> depId = Optional.empty();

    private boolean active;
}

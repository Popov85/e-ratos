package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
public class GroupInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long groupId;

    @NotBlank( message = "{dto.string.required}")
    @Size(min = 1, max = 100, message = "{dto.string.invalid}")
    private String name;

    private boolean enabled;
}

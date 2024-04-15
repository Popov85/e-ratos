package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long orgId;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 3, max = 100, message = "{dto.string.invalid}")
    private String name;

}

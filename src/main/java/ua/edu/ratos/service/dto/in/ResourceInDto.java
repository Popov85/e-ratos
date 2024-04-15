package ua.edu.ratos.service.dto.in;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@ToString(callSuper = true)
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResourceInDto {

    private Long resourceId;

    @NotBlank(message = "{dto.string.required}")
    @URL(protocol = "https", message = "{dto.string.url}")
    @Size(min = 8, max = 1000, message = "{dto.string.invalid}")
    private String link;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 1, max = 200, message = "{dto.string.invalid}")
    private String description;

    @NotBlank(message = "{dto.string.required}")
    @Size(min = 3, max = 20, message = "{dto.string.invalid}")
    private String type;

    @Range(min = 1, max = 640, message = "{dto.range.invalid}")
    private short width;

    @Range(min = 1, max = 480, message = "{dto.range.invalid}")
    private short height;
}

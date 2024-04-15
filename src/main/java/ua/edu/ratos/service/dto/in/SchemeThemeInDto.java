package ua.edu.ratos.service.dto.in;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.Set;


@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SchemeThemeInDto {

    private Long schemeThemeId;

    private Long schemeId;

    @Positive(message = "{dto.fk.required}")
    private long themeId;

    @PositiveOrZero(message = "{dto.value.positiveOrZero}")
    private short order;

    @NotEmpty(message = "{dto.collection.required}")
    @Size(min = 1, max = 5, message = "{dto.collection.invalid}")
    private Set<@Valid SchemeThemeSettingsInDto> settings;
}

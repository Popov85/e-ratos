package ua.edu.ratos.service.dto.in;

import lombok.*;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@ToString
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SchemeThemeSettingsInDto {

    // nullable for new objects
    private Long schemeThemeSettingsId;

    // nullable for new objects
    private Long schemeThemeId;

    @Positive( message = "{dto.fk.required}")
    private long questionTypeId;

    @PositiveOrZero( message = "{dto.value.positiveOrZero}")
    private short level1;

    @PositiveOrZero(message = "{dto.value.positiveOrZero}")
    private short level2;

    @PositiveOrZero(message = "{dto.value.positiveOrZero}")
    private short level3;
}

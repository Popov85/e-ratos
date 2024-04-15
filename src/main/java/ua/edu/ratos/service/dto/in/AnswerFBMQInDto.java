package ua.edu.ratos.service.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import jakarta.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Accessors(chain = true)
/**
 * @link http://www.bytestree.com/spring/spring-boot-restful-web-services-validation-hibernate-validator/
 * @link https://habr.com/post/343960/
 */
public class AnswerFBMQInDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long answerId;

    @NotBlank( message = "{dto.string.required}")
    @Size(min = 1, max = 200, message = "{dto.string.invalid}")
    private String phrase;

    @Range(min=1, max=20, message = "{dto.range.invalid}")
    private byte occurrence;

    @Positive(message = "{dto.fk.required}")
    private long setId;

    @NotEmpty( message = "{dto.collection.required}")
    @Size(min = 1, max = 20, message = "{dto.collection.invalid}")
    private Set<Long> phrasesIds;
}

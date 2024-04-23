package ua.edu.ratos.service.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserOutDto {

    private String name;

    private String surname;

    private String email;

    private boolean active;

    @JsonProperty("role")
    private Set<String> roles;
}

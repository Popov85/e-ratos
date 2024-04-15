package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    private static final Long serialVersionUID = 1L;

    @EmbeddedId
    private UserRoleId userRoleId = new UserRoleId();

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}

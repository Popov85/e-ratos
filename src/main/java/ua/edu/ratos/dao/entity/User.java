package ua.edu.ratos.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ua.edu.ratos.dao.converter.JsonToSetConverter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private char[] password;

    @Column(name="is_active")
    private boolean active;

    @Convert(converter = JsonToSetConverter.class)
    @Column(name = "roles")
    private Set<String> roles = new HashSet<>();

}

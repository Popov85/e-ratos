package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(exclude = {"scheme", "user"})
@Entity
@Table(name="session_preserved")
public class SessionPreserved implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name="uuid")
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="data")
    private String data;

    @Column(name="progress")
    private double progress;

    @Column(name = "when_preserved")
    private LocalDateTime whenPreserved;
}

package ua.edu.ratos.dao.entity.game;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ua.edu.ratos.dao.entity.Student;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(name = "game_bonus")
public class Bonus implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "bonus_id")
    private Long bonusId;

    @ManyToOne
    @JoinColumn(name = "stud_id", updatable = false)
    private Student student;

    @Column(name = "bonus")
    private int bonus;

    @Column(name="when_granted")
    private LocalDateTime whenGranted;
}

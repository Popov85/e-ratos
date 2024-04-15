package ua.edu.ratos.dao.entity.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ua.edu.ratos.dao.entity.Student;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@Entity
@Table(name = "game_week_won")
public class Wins implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "won_id")
    private Long winId;

    @ManyToOne
    @JoinColumn(name = "stud_id", updatable = false)
    private Student student;

    // How many points a student scored when he won a week
    // The total value a moment before some designated time each week
    // e.g. 23:59:59.999 each Saturday/Sunday
    @Column(name = "won_points")
    private int wonPoints;

    @Column(name = "won_bonuses")
    private int wonBonuses;

    @Column(name = "won_time_spent")
    private long wonTimeSpent;

    // Date of weekly reset
    @Column(name="won_date")
    private LocalDate wonDate;
}

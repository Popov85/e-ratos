package ua.edu.ratos.dao.entity.game;

import lombok.*;
import ua.edu.ratos.dao.entity.Student;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Weekly resettable table.
 * Developer should ensure to reset this table say at 23:59 each Sunday.
 * Based on this table we calculate weekly winners.
 * To be a weekly winner you have to fall into TOP n% best results.
 * Results are ranked based on weekly points and bonuses.
 * If some students have equal points and bonuses,
 * then the winners are those who spent less time during this week's learning sessions.
 */
@Setter
@Getter
@ToString(exclude = "stud")
@Entity
@Table(name = "game_week")
public class Week implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "stud_id")
    private Long weekId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "stud_id")
    private Student stud;

    @Column(name = "week_points")
    private int weekPoints;

    @Column(name = "week_bonuses")
    private int weekBonuses;

    @Column(name = "week_strike")
    private int weekStrike;

    @Column(name = "week_time_spent")
    private long weekTimeSpent;

}

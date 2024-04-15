package ua.edu.ratos.dao.entity.game;

import lombok.*;
import ua.edu.ratos.dao.entity.Student;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "stud_id")
    private Long gameId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "stud_id")
    private Student stud;

    // How many points a student has scored
    @Column(name = "total_points")
    private int totalPoints;

    // How many bonuses for all time a student has earned
    @Column(name = "total_bonuses")
    private int totalBonuses;

    // How many weeks a student has won
    @Column(name = "total_weeks_won")
    private int totalWins;
}

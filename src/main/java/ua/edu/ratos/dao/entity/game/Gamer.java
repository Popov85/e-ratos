package ua.edu.ratos.dao.entity.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ua.edu.ratos.dao.entity.*;
import ua.edu.ratos.dao.entity.Clazz;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@ToString(exclude = {"studentClass", "faculty", "organisation", "game", "week"})
@Entity
@Table(name = "student")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Gamer implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "stud_id")
    private Long studId;

    @MapsId
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "stud_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Clazz studentClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id")
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private Organisation organisation;

    @Column(name = "entrance_year")
    private int entranceYear;

    @OneToOne(mappedBy = "stud")
    private Game game;

    @OneToOne(mappedBy = "stud")
    private Week week;

    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    public Optional<Week> getWeek() {
        return Optional.ofNullable(week);
    }
}

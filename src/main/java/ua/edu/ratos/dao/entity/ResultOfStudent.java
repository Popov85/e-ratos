package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ua.edu.ratos.dao.entity.lms.LMS;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString(exclude = {"scheme", "student", "lms", "resultTheme"})
@Entity
@Table(name = "result")
public class ResultOfStudent implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "scheme_id", updatable = false)
    protected Scheme scheme;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    protected Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_id", updatable = false)
    private Department department;

    @Column(name = "percent")
    private double percent;

    @Column(name = "grade")
    private double grade;

    @Column(name = "is_passed")
    private boolean passed;

    @Column(name = "is_points_granted")
    private boolean points;

    @Column(name = "session_ended")
    private OffsetDateTime sessionEnded;

    @Column(name = "session_lasted")
    private long sessionLasted;

    @Column(name = "is_timeouted")
    private boolean timeOuted;

    @Column(name = "is_cancelled")
    private boolean cancelled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lms_id")
    private LMS lms;

    @OneToMany(mappedBy = "result", cascade = {CascadeType.ALL})
    private List<ResultOfStudentTheme> resultTheme = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "result_id")
    private ResultOfStudentDetails resultDetails;

    public Optional<LMS> getLms() {
        return Optional.ofNullable(lms);
    }
}

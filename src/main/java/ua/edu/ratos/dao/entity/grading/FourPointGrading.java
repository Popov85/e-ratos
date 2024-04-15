package ua.edu.ratos.dao.entity.grading;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.service.session.grade.GradedResult;
import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"staff", "department", "grading"})
@Entity
@Table(name = "four_point")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class FourPointGrading implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "four_id")
    private Long fourId;

    @Column(name = "name")
    private String name;

    @Column(name = "threshold_3")
    private byte threshold3;

    @Column(name = "threshold_4")
    private byte threshold4;

    @Column(name = "threshold_5")
    private byte threshold5;

    @Column(name="is_default")
    private boolean isDefault;

    @Column(name = "is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grading_id")
    private Grading grading;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to")
    private Department department;


    public GradedResult grade(final double percent) {
        byte grade;
        if (percent < threshold3) {
            grade = 2;
        } else if (percent >= threshold3 && percent < threshold4) {
            grade = 3;
        } else if (percent >= threshold4 && percent < threshold5) {
            grade = 4;
        } else {
            grade = 5;
        }
        return new GradedResult(grade > 2, grade);
    }
}

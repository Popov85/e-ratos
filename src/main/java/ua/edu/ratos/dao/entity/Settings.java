package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"staff", "department"})
@Entity
@Table(name = "settings")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class Settings implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "set_id")
    private Long setId;

    @Column(name = "name")
    private String name;

    /**
     * Negative int is used for "not limited in time", default - do not limit
     */
    @Column(name = "seconds_per_question")
    private int secondsPerQuestion;

    /**
     * Specifies, whether or not to limit a single question in time, default - do not limit
     */
    @Column(name = "strict_seconds_per_question")
    private boolean strictControlTimePerQuestion;

    /**
     * Negative short is used for "all questions per sheet" option
     */
    @Column(name = "questions_per_sheet")
    private short questionsPerSheet;

    @Column(name = "days_keep_result_details")
    private short daysKeepResultDetails;

    @Column(name = "level_2_coefficient")
    private float level2Coefficient;

    @Column(name = "level_3_coefficient")
    private float level3Coefficient;

    @Column(name="is_default")
    private boolean isDefault;

    @Column(name = "is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to")
    private Department department;

}

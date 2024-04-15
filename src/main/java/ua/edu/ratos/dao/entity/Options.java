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
@Table(name = "options")
@Cacheable
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
public class Options implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "opt_id")
    private Long optId;

    @Column(name = "name")
    private String name;

    // -- Session--

    @Column(name = "display_questions_left")
    private boolean displayQuestionsLeft;

    @Column(name = "display_batches_left")
    private boolean displayBatchesLeft;

    @Column(name = "display_current_score")
    private boolean displayCurrentScore;

    @Column(name = "display_effective_score")
    private boolean displayEffectiveScore;

    @Column(name = "display_progress")
    private boolean displayProgress;

    // Messages for 100% result on a batch
    @Column(name = "display_motivational_messages")
    private boolean displayMotivationalMessages;

    //-- End--

    @Column(name = "display_result_score")
    private boolean displayResultScore;

    @Column(name = "display_result_mark")
    private boolean displayResultMark;

    @Column(name = "display_time_spent")
    private boolean displayTimeSpent;

    @Column(name = "display_result_themes")
    private boolean displayResultOnThemes;

    @Column(name="display_result_questions")
    private boolean displayResultOnQuestions;

    // -- Technical--

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

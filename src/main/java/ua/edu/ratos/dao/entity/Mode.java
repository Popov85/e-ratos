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
@Table(name = "mode")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
@SuppressWarnings("SpellCheckingInspection")
public class Mode implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "mode_id")
    private Long modeId;

    @Column(name="name")
    private String name;

    @Column(name="is_helpable")
    private boolean helpable;

    @Column(name="is_pyramid")
    private boolean pyramid;

    @Column(name="is_skipable")
    private boolean skipable;

    /**
     * Whether or not to allow to evaluate and show
     * right answers in the course of learning session
     */
    @Column(name="is_rightans")
    private boolean rightAnswer;

    @Column(name="is_preservable")
    private boolean preservable;

    @Column(name="is_pauseable")
    private boolean pauseable;

    @Column(name="is_reportable")
    private boolean reportable;

    @Column(name="is_starrable")
    private boolean starrable;

    @Column(name="is_deleted")
    private boolean deleted;

    @Column(name="is_default")
    private boolean defaultMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to")
    private Department department;
}

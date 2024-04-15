package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.question.Question;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"course", "staff", "questions", "access", "department"})
@Entity
@Table(name="theme")
@Where(clause = "is_deleted = 0")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    public Theme(Long themeId, String name) {
        this.themeId = themeId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="theme_id")
    private Long themeId;

    @Column(name="name")
    private String name;

    @Column(name="is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_id")
    private Access access;

    @Column(name = "created", updatable = false)
    private OffsetDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to", updatable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private Staff staff;

    @OneToMany(mappedBy = "theme")
    private Set<Question> questions = new HashSet<>();
}

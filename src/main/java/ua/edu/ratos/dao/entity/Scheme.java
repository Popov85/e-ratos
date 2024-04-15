package ua.edu.ratos.dao.entity;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.grading.Grading;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"strategy", "settings", "mode", "options", "grading", "course", "staff", "department", "themes", "groups", "access"})
@Entity
@Table(name="scheme")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
@NoArgsConstructor
public class Scheme implements Serializable {

    private static final Long serialVersionUID = 1L;

    // Used for JPA repositories for projections
    public Scheme(Long schemeId, String name) {
        this.schemeId = schemeId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="scheme_id")
    private Long schemeId;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settings_id")
    private Settings settings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mode_id")
    private Mode mode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "options_id")
    private Options options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grading_id")
    private Grading grading;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Staff staff;

    // Scheme remains to belong to the department
    // even if staff changes department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to")
    private Department department;

    @Column(name="created", updatable = false)
    private OffsetDateTime created = OffsetDateTime.now();

    @Column(name="is_active")
    private boolean active;

    @Column(name="lms_only")
    private boolean lmsOnly;

    @Column(name="is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_id")
    private Access access;

    @OrderColumn(name = "theme_order")
    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.EXTRA)
    private List<SchemeTheme> themes = new ArrayList<>();

    public void addSchemeTheme(@NonNull SchemeTheme schemeTheme) {
        this.themes.add(schemeTheme);
        schemeTheme.setScheme(this);
    }

    public void clearSchemeTheme() {
        this.themes.clear();
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "group_scheme", joinColumns = @JoinColumn(name = "scheme_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.EXTRA)
    private Set<Group> groups = new HashSet<>();

    public void addGroup(Group group) {
        this.groups.add(group);
    }
}

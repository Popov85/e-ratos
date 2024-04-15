package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.lms.LMSCourse;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString(exclude = {"staff", "department", "access"})
@Entity
@Table(name = "course")
@DynamicUpdate
@NoArgsConstructor
@Where(clause = "is_deleted = 0")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    public Course(Long courseId, String name) {
        this.courseId = courseId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private Staff staff;

    // Course remains to belong to the department
    // even if staff changes department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to", updatable = false)
    private Department department;

    @Column(name = "created", updatable = false)
    private OffsetDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_id")
    private Access access;

    @Column(name="is_deleted")
    private boolean deleted;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
    private LMSCourse lmsCourse;

    // TODO: add setter to sync both parts of OnToOne!
}

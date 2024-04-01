package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"staff", "department", "students"})
@Entity
@Table(name = "u_groups")
@Where(clause = "is_deleted = 0")
@DynamicUpdate
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "name")
    private String name;

    @Column(name="is_enabled")
    private boolean enabled;

    @Column(name="is_deleted")
    private boolean deleted;

    @Column(name="created")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to")
    private Department department;

    // read-only here
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "student_group", joinColumns = {@JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "stud_id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Student> students = new HashSet<>();
}

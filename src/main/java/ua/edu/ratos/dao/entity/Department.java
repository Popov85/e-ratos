package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"faculty"})
@Entity
@Table(name = "department")
@Where(clause = "is_deleted = 0")
@NoArgsConstructor
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    public Department(Long depId, String name) {
        this.depId = depId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "dep_id")
    private Long depId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_id")
    protected Faculty faculty;

    @Column(name = "is_deleted")
    private boolean deleted;
}

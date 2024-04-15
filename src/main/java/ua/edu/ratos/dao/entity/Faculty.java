package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString(exclude = {"organisation"})
@Entity
@Table(name = "faculty")
@Where(clause = "is_deleted = 0")
@NoArgsConstructor
public class Faculty implements Serializable {

    private static final long serialVersionUID = 1L;

    public Faculty(Long facId, String name) {
        this.facId = facId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "fac_id")
    private Long facId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private Organisation organisation;

    @Column(name = "is_deleted")
    private boolean deleted;
}

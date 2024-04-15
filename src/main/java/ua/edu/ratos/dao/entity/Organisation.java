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
@ToString
@Entity
@Table(name = "organisation")
@Where(clause = "is_deleted = 0")
@NoArgsConstructor
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    public Organisation(Long orgId, String name) {
        this.orgId = orgId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private boolean deleted;
}

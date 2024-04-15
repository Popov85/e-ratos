package ua.edu.ratos.dao.entity.grading;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = "freePointGrading")
@Entity
@Table(name = "scheme_free_point")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchemeFreePoint implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "scheme_id")
    private Long schemeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_point_id")
    private FreePointGrading freePointGrading;
}

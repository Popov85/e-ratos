package ua.edu.ratos.dao.entity.grading;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Pre-defined types of grading systems:
 * 1) Four-point: traditional scale for CIS countries {2, 3, 4, 5}, grade is calculated based on thresholds
 * 2) Two-point: traditional scale for CIS countries in universities {1, 0} or {passed, not passed}, grade is calculated based on threshold
 * 3) Free-point: universal discrete scale {min, ..., max}, grade is calculated based on min and max values
 * 4) To be continued...
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "grading")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Grading implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "grading_id")
    private Long gradingId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}

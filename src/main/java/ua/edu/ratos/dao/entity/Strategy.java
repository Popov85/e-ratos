package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Pre-defined strategies:
 * Theme -> Type -> Level -> Question
 * 1) Default: randomised irrespective to question type and level, but observe themes order
 * 2) Full randomized: randomized irrespective to theme order, question type or level
 * 3) Types then levels.
 * 4) To be continued...
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "strategy")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Strategy implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "str_id")
    private Long strId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}

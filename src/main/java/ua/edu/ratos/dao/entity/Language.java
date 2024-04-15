package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity
@Table(name="language")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Language implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "lang_id")
    private Long langId;

    @Column(name = "name")
    private String name;

    @Column(name = "eng_abbreviation")
    private String abbreviation;
}

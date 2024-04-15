package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@ToString
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="question_type")
public class QuestionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "eng_abbreviation")
    private String abbreviation;

    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionType that = (QuestionType) o;
        return Objects.equals(abbreviation, that.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abbreviation);
    }
}

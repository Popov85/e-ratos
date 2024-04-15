package ua.edu.ratos.dao.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ComplaintId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "ctype_id")
    private Long ctypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplaintId that = (ComplaintId) o;
        return Objects.equals(questionId, that.questionId) &&
                Objects.equals(ctypeId, that.ctypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, ctypeId);
    }
}

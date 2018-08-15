package ua.edu.ratos.domain.entity.answer;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.domain.entity.question.QuestionFillBlankMultiple;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Free-word answerIds, multiple blanks to fill acceptable
 * @author Andrey P.
 */
@Setter
@Getter
@ToString(exclude = {"question", "settings", "acceptedPhrases"})
@Entity
@Table(name = "answer_fbmq")
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class AnswerFillBlankMultiple{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="answer_id")
    private Long answerId;

    @Column(name="phrase")
    private String phrase;

    @Column(name="occurrence")
    private byte occurrence = 1;

    @Column(name="is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionFillBlankMultiple question;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private SettingsAnswerFillBlank settings;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "fbmq_phrase", joinColumns = {@JoinColumn(name = "answer_id") }, inverseJoinColumns = { @JoinColumn(name = "phrase_id")})
    private Set<AcceptedPhrase> acceptedPhrases = new HashSet<>();

    public void addPhrase(@NonNull AcceptedPhrase phrase) {
        this.acceptedPhrases.add(phrase);
    }

    public void removePhrase(@NonNull AcceptedPhrase phrase) {
        this.acceptedPhrases.remove(phrase);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerFillBlankMultiple that = (AnswerFillBlankMultiple) o;
        return occurrence == that.occurrence &&
                Objects.equals(phrase, that.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phrase, occurrence);
    }
}

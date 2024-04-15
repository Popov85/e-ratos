package ua.edu.ratos.dao.entity.answer;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.Phrase;
import ua.edu.ratos.dao.entity.SettingsFB;
import ua.edu.ratos.dao.entity.question.QuestionFBMQ;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Free-word answer, multiple blanks to fill in by a user are acceptable
 * @author Andrey P.
 */
@Setter
@Getter
@ToString(exclude = {"question", "settings", "acceptedPhrases"})
@Entity
@Table(name = "answer_fbmq")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class AnswerFBMQ implements Serializable {

    private static final Long serialVersionUID = 1L;

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
    private QuestionFBMQ question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id")
    private SettingsFB settings;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "fbmq_phrase", joinColumns = {@JoinColumn(name = "answer_id") }, inverseJoinColumns = { @JoinColumn(name = "phrase_id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Phrase> acceptedPhrases = new HashSet<>();

    public void addPhrase(@NonNull Phrase phrase) {
        this.acceptedPhrases.add(phrase);
    }

}

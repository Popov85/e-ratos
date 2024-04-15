package ua.edu.ratos.dao.entity.answer;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.Phrase;
import ua.edu.ratos.dao.entity.question.QuestionMQ;
import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString(exclude = {"question"})
@Entity
@Table(name = "answer_mq")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class AnswerMQ implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="answer_id")
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "left_phrase_id")
    private Phrase leftPhrase;

    @ManyToOne
    @JoinColumn(name = "right_phrase_id")
    private Phrase rightPhrase;

    @Column(name="is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionMQ question;
}

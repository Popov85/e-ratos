package ua.edu.ratos.dao.entity.question;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import ua.edu.ratos.dao.entity.answer.AnswerMQ;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Match.
 * Matching question consist of two columns, typically one column is on the left and one column is on the right.
 * We will refer to the left side as 'Clues' and the right side as 'Matches'.
 * The objective is to pair the clues on the left side with their matches on the right.
 * These can be created using help on both sides or a mix of help with media,
 * such as images, audio or video.
 *
 * @see <a href="https://www.classmarker.com/learn/question-types/matching-questions/">Match</a>
 * @author Andrey P.
 */

@Slf4j
@Setter
@Getter
@Entity
@Cacheable
@DiscriminatorValue(value = "4")
@DynamicUpdate
public class QuestionMQ extends Question {

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AnswerMQ> answers = new ArrayList<>();

    public void addAnswer(AnswerMQ answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }
}

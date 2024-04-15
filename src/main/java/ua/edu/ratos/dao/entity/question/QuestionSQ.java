package ua.edu.ratos.dao.entity.question;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import ua.edu.ratos.dao.entity.answer.AnswerSQ;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Process.
 * This type of question matches the right sequence/steps/phases/stages to a control.
 * Objective is to check the provided response in the right sequence.
 * Wrong sequence of elements leads to the negative evaluation outcome.
 * @author Andrey P.
 */

@Setter
@Getter
@Entity
@Cacheable
@DiscriminatorValue(value = "5")
@DynamicUpdate
public class QuestionSQ extends Question {

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AnswerSQ> answers = new ArrayList<>();

    public void addAnswer(AnswerSQ answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

}

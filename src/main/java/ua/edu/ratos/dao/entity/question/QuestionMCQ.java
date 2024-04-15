package ua.edu.ratos.dao.entity.question;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import ua.edu.ratos.dao.entity.answer.AnswerMCQ;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString(callSuper = true, exclude = {"answers"})
@Entity
@Cacheable
@DiscriminatorValue(value = "1")
@DynamicUpdate
public class QuestionMCQ extends Question {

    @Transient
    private boolean isSingle;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AnswerMCQ> answers = new ArrayList<>();

    public void addAnswer(AnswerMCQ answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    /**
     * Creates a new empty Question object (for parsers)
     * @return newly created empty Question
     */
    public static QuestionMCQ createEmpty() {
        QuestionMCQ q = new QuestionMCQ();
        q.setQuestion("");
        q.setLevel((byte)1);
        q.setAnswers(new ArrayList<>());
        return q;
    }

    /**
     * Checks whether this question contains only one correct answerIds or multiple correct answers
     * @return true or false
     */
    public boolean isSingle() {
        if (!isValid()) throw new RuntimeException("Invalid questionId = "+this.questionId);
        int counter = 0;
        for (AnswerMCQ answer : answers) {
            if (!answer.isValid()) throw new RuntimeException("Invalid answerIds");
            if (answer.getPercent() != 0) counter++;
        }
        return (counter > 1) ? false : true;
    }

    public boolean isValid() {
        if (!super.isValid()) return false;
        if (this.answers == null) return false;
        if (this.answers.isEmpty()) return false;
        if (this.answers.size()<2) return false;
        if (!isValid(this.answers)) return false;
        return true;
    }

    private boolean isValid(@NonNull List<AnswerMCQ> list) {
        int sum = 0;
        for (AnswerMCQ a : list) {
            if (a == null) return false;
            if (!a.isValid()) return false;
            if (a.getPercent()==0 && a.isRequired()) a.setRequired(false);
            sum+=a.getPercent();
        }
        if (sum!=100) return false;
        return true;
    }
}

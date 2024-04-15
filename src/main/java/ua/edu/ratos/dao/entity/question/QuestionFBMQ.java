package ua.edu.ratos.dao.entity.question;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import ua.edu.ratos.dao.entity.answer.AnswerFBMQ;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Cacheable
@DiscriminatorValue(value = "3")
@DynamicUpdate
public class QuestionFBMQ extends Question{

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AnswerFBMQ> answers = new HashSet<>();

    public void addAnswer(AnswerFBMQ answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    public void removeAnswer(AnswerFBMQ answer) {
        this.answers.remove(answer);
        answer.setQuestion(null);
    }
}

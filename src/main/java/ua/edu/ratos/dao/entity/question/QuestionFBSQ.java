package ua.edu.ratos.dao.entity.question;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ua.edu.ratos.dao.entity.answer.AnswerFBSQ;

import jakarta.persistence.*;

@Setter
@Getter
@Entity
@Cacheable
@DiscriminatorValue(value = "2")
@DynamicUpdate
//@NoArgsConstructor
public class QuestionFBSQ extends Question {

    // Actually, "optional = false", but for testing purposes (to simplify IT tests set-ups)
    @OneToOne(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = true)
    private AnswerFBSQ answer;

    public void addAnswer(AnswerFBSQ answer) {
        this.answer = answer;
        answer.setQuestion(this);
    }
}

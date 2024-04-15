package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.edu.ratos.dao.entity.question.Question;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(exclude = {"user", "question"})
@Entity
@Table(name = "user_question_starred")
public class UserQuestionStarred implements Serializable {

    private static final Long serialVersionUID = 1L;

    @EmbeddedId
    private UserQuestionStarredId userQuestionStarredId = new UserQuestionStarredId();

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("questionId")
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "star")
    private byte star;

    @Column(name="when_starred")
    private LocalDateTime whenStarred;
}

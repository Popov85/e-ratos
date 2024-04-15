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
@ToString(exclude = {"question", "complaintType", "department"})
@Entity
@Table(name = "complaint")
public class Complaint implements Serializable {

    private static final Long serialVersionUID = 1L;

    @EmbeddedId
    private ComplaintId complaintId = new ComplaintId();

    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @MapsId("ctypeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctype_id")
    private ComplaintType complaintType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_id")
    private Department department;

    // When complained last time
    @Column(name="last_complained")
    private LocalDateTime lastComplained;

    // How many times they complained about this pair question+complaintType
    @Column(name="times_complained")
    private int timesComplained;

}

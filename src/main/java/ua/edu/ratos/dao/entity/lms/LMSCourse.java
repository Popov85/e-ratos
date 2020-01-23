package ua.edu.ratos.dao.entity.lms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.edu.ratos.dao.entity.Course;
import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"course", "lms"})
@Entity
@Table(name = "lms_course")
public class LMSCourse {

    @Id
    @Column(name = "course_id")
    private Long courseId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lms_id")
    private LMS lms;
}

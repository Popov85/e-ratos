package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString
@Entity
@Table(name = "student_group")
public class StudentGroup implements Serializable {

    private static final Long serialVersionUID = 1L;

    @EmbeddedId
    private StudentGroupId studentGroupId = new StudentGroupId();

    @MapsId("studId")
    @ManyToOne
    @JoinColumn(name = "stud_id")
    private Student student;

    @MapsId("groupId")
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}

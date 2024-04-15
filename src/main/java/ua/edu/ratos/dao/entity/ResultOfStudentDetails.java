package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(exclude = "jsonData")
@Entity
@Table(name = "result_details")
public class ResultOfStudentDetails implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "data")
    private String jsonData;

    @Column(name = "when_remove")
    private LocalDateTime whenRemove;

}

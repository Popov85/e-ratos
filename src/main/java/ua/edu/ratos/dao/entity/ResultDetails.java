package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(exclude = "result")
@Entity
@Table(name = "result_details")
public class ResultDetails implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "detail_id")
    private Long detailId;

    @MapsId
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "detail_id")
    private Result result;

    @Column(name = "data")
    private String jsonData;

    @Column(name = "when_remove", columnDefinition = "TIMESTAMP")
    private LocalDateTime whenRemove;

}

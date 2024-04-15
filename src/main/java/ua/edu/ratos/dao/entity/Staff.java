package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"position", "department"})
@Entity
@Table(name = "staff")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "staff_id")
    private Long staffId;

    @MapsId
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "staff_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pos_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_id")
    private Department department;

}

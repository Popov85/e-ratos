package ua.edu.ratos.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "resource")
public class Resource {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long resourceId;

    @Column(name="hyperlink")
    private String link;

    @Column(name="description")
    private String description;
}

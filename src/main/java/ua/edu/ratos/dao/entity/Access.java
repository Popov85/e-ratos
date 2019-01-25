package ua.edu.ratos.dao.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Access level defines who can edit tables {course, scheme, theme};
 * There are 2 types of access level so far: dep-private and private;
 * <b>Dep-private</b> access level allows editing of an element by any department instructor
 * <b>Private </b>access allows editing of an element only by the owner (creator).
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_level")
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "access_id")
    private Long accessId;

    @Column(name = "name")
    private String name;

}

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
@Table(name = "group_scheme")
public class GroupScheme implements Serializable {

    private static final Long serialVersionUID = 1L;

    @EmbeddedId
    private GroupSchemeId groupSchemeId = new GroupSchemeId();

    @MapsId("groupId")
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @MapsId("schemeId")
    @ManyToOne
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;

}

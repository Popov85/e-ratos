package ua.edu.ratos.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GroupSchemeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "scheme_id")
    private Long schemeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupSchemeId that = (GroupSchemeId) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(schemeId, that.schemeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, schemeId);
    }
}

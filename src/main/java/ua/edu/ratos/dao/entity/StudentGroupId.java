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
public class StudentGroupId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "stud_id")
    private Long studId;

    @Column(name = "group_id")
    private Long groupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGroupId that = (StudentGroupId) o;
        return Objects.equals(studId, that.studId) &&
                Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studId, groupId);
    }
}

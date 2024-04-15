package ua.edu.ratos.dao.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ResultThemeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "theme_id")
    private Long themeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultThemeId that = (ResultThemeId) o;
        return Objects.equals(resultId, that.resultId) &&
                Objects.equals(themeId, that.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, themeId);
    }
}

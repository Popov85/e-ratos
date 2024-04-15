package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString(exclude = {"result", "theme"})
@Entity
@Table(name = "result_theme")
public class ResultTheme implements Serializable {

    private static final Long serialVersionUID = 1L;

    @EmbeddedId
    private ResultThemeId resultThemeId = new ResultThemeId();

    @MapsId("resultId")
    @OneToOne
    @JoinColumn(name = "result_id")
    private Result result;

    @MapsId("themeId")
    @OneToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name = "percent")
    private double percent;

    @Column(name = "quantity")
    private int quantity;
}

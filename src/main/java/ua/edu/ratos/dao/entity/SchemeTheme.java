package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"scheme", "theme", "settings"})
@Entity
@Table(name="scheme_theme")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchemeTheme implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="scheme_theme_id")
    private Long schemeThemeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name="theme_order")
    private short order;

    @OneToMany(mappedBy = "schemeTheme", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SchemeThemeSettings> settings = new HashSet<>();

    public void addSchemeThemeSettings(@NonNull SchemeThemeSettings schemeThemeSettings) {
        this.settings.add(schemeThemeSettings);
        schemeThemeSettings.setSchemeTheme(this);
    }

}

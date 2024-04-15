package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@ToString(exclude = {"lang", "staff"})
@Entity
@Table(name = "settings_fbq")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class SettingsFB implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="set_id")
    private Long settingsId;

    @Column(name="name")
    private String name;

    @Column(name="words_limit")
    private short wordsLimit = 1;

    @Column(name="symbols_limit")
    private short symbolsLimit = 1;

    @Column(name="is_numeric")
    private boolean isNumeric;

    @Column(name="is_typo_allowed")
    private boolean isTypoAllowed;

    @Column(name="is_case_sensitive")
    private boolean isCaseSensitive;

    @Column(name="is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id")
    private Language lang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;
}

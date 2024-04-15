package ua.edu.ratos.dao.entity.lms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.Organisation;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"organisation", "credentials", "ltiVersion"})
@Entity
@Table(name = "lms")
@Where(clause = "is_deleted = 0")
@DynamicUpdate
@NoArgsConstructor
public class LMS implements Serializable {

    private static final Long serialVersionUID = 1L;

    public LMS(Long lmsId, String name) {
        this.lmsId = lmsId;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "lms_id")
    private Long lmsId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", updatable = false)
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lti_version_id")
    private LTIVersion ltiVersion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lms_id")
    private LTICredentials credentials;

    @Column(name="is_deleted")
    private boolean deleted;
}

package ua.edu.ratos.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = "staff")
@Entity
@Table(name = "resource")
@Where(clause = "is_deleted = 0")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicUpdate
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="resource_id")
    private Long resourceId;

    @Column(name="hyperlink")
    private String link;

    @Column(name="description")
    private String description;

    @Column(name="type")
    private String type;

    @Column(name="width")
    private short width;

    @Column(name="height")
    private short height;

    @Column(name="last_used", nullable = false)
    private OffsetDateTime lastUsed = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name="is_deleted")
    private boolean deleted;

    public Resource(String link, String description) {
        this.link = link;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(link, resource.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}

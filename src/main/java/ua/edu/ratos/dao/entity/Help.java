package ua.edu.ratos.dao.entity;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"staff", "resources"})
@Entity
@Table(name = "help")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class Help implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "help_id", updatable = false, nullable = false)
    private Long helpId;

    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String help;

    @Column(name="is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", updatable = false, nullable = false)
    private Staff staff;

    // Technically many Resources can be associated with a Help, but we go for only one for now
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "help_resource", joinColumns = @JoinColumn(name = "help_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> resources = new HashSet<>();

    public void addResource(Resource resource) {
        if (!this.resources.isEmpty())
            throw new IllegalStateException("Currently, only one resource can be associated with a help");
        this.resources.add(resource);
    }

    public void clearResources() {
        this.resources.clear();
    }

    public Optional<Resource> getResource() {
        Resource resource = null;
        if (this.resources!=null && !this.resources.isEmpty()) {
            resource = this.resources.iterator().next();
        }
        return Optional.ofNullable(resource);
    }
}

package ua.edu.ratos.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"staff", "resources"})
@NoArgsConstructor
@Entity
@Table(name = "phrase")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
public class Phrase implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="phrase_id", nullable = false, updatable = false)
    private Long phraseId;

    @Column(name="phrase")
    private String phrase;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false, updatable = false)
    private Staff staff;

    @JsonIgnore
    @Column(name="last_used", nullable = false)
    private LocalDateTime lastUsed = LocalDateTime.now();

    @Column(name="is_deleted")
    private boolean deleted;

     // One-to-one actually, but for the sake of simplicity (so that not to create a separate class like PhraseResource), we use Many-to-many
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "phrase_resource", joinColumns = @JoinColumn(name = "phrase_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> resources = new HashSet<>();

    public Phrase(String phrase) {
        this.phrase = phrase;
    }

    public void addResource(Resource resource) {
        if (!this.resources.isEmpty())
            throw new IllegalStateException("Currently, only one resource can be associated with a phrase");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase1 = (Phrase) o;
        return Objects.equals(phrase, phrase1.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phrase);
    }
}

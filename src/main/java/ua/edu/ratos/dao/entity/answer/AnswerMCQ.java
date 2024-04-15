package ua.edu.ratos.dao.entity.answer;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.question.QuestionMCQ;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@ToString(exclude = {"question", "resources"})
@NoArgsConstructor
@Entity
@Table(name = "answer_mcq")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "is_deleted = 0")
@DynamicUpdate
public class AnswerMCQ implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "percent")
    private short percent;

    @Column(name = "is_required")
    private boolean isRequired;

    @Column(name = "is_deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionMCQ question;

    // One-to-one actually, but for the sake of simplicity (so that not to create a separate class like AnswerResource), we use Many-to-many
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "answer_mcq_resource", joinColumns = @JoinColumn(name = "answer_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> resources = new HashSet<>();

    public void addResource(Resource resource) {
        if (!this.resources.isEmpty())
            throw new IllegalStateException("Currently, only one resource can be associated with an answer");
        this.resources.add(resource);
    }

    public void clearResources() {
        this.resources.clear();
    }

    public Optional<Resource> getResource() {
        Resource resource = null;
        if (this.resources != null && !this.resources.isEmpty()) {
            resource = this.resources.iterator().next();
        }
        return Optional.ofNullable(resource);
    }

    public boolean isValid() {
        if (answer == null) return false;
        if (answer.isEmpty()) return false;
        if (percent < 0 || percent > 100) return false;
        if (percent == 0 && isRequired) isRequired = false;
        return true;
    }
}

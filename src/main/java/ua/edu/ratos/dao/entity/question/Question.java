package ua.edu.ratos.dao.entity.question;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import ua.edu.ratos.dao.entity.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "question")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_id", discriminatorType = DiscriminatorType.INTEGER)
@Where(clause = "is_deleted = 0")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "question_id", updatable = false, nullable = false)
    protected Long questionId;

    @Column(name = "title")
    protected String question;

    @Column(name = "level")
    protected byte level;
    /**
     * Required questions are guaranteed to be included
     * into the learning session within their theme!
     * Do not overuse this feature!
     * 1-5 required questions per theme of total 10 for session is OK
     */
    @Column(name = "is_required")
    protected boolean required;

    @Column(name = "is_deleted")
    protected boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", updatable = false)
    protected Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    protected QuestionType type;

    /**
     * Only applicable to FBMQ & MQ (in MCQ's partiality is managed by isRequired flag);
     * Specifies whether partial correct response is allowed or not;
     * For example, you matched correctly 3 out of 5 phrases in MQ, then you would get 0 if false, or 3/5 if true;
     * For example, you entered correctly 1 out of 3 phrases in FBMQ, then you would get 0 if false, or 1/3 if true;
     */
    @Column(name = "is_partial")
    protected boolean partialResponseAllowed;

    // Technically many Helps can be associated with a Question, but we go for only one for now
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "question_help", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "help_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    protected Set<Help> helps = new HashSet<>();

    // Often many Resources can be associated with a Question (e.g. an image plus an audio), we let this opportunity to remain
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "question_resource", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    protected Set<Resource> resources = new HashSet<>();

    public Question(String question, byte level) {
        this.question = question;
        this.level = level;
    }

    // Currently only one help is allowed!
    public void addHelp(Help help) {
        this.helps.clear();
        this.helps.add(help);
    }

    public void clearHelps() {
        this.helps.clear();
    }

    public Optional<Help> getHelp() {
        Help help = null;
        if (this.helps!=null && !this.helps.isEmpty()) {
            help = this.helps.iterator().next();
        }
        return Optional.ofNullable(help);
    }

    // Currently only one resource is allowed!
    public void addResource(Resource resource) {
        this.resources.clear();
        this.resources.add(resource);
    }

    public void clearResources() {
        this.resources.clear();
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public Optional<Resource> getResource() {
        Resource resource = null;
        if (this.resources!=null && !this.resources.isEmpty()) {
            resource = this.resources.iterator().next();
        }
        return Optional.ofNullable(resource);
    }

    public boolean isValid() {
        if (this.question == null) return false;
        if (this.question.isEmpty()) return false;
        if (this.level < 1 || this.level > 3) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", question='" + question + '\'' +
                ", level=" + level +
                '}';
    }
}

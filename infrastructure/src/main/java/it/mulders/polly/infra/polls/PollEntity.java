package it.mulders.polly.infra.polls;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Entity
@NamedQuery(
        name = "Poll.findBySlug",
        query = "select p from PollEntity p left join fetch p.options as po where p.slug = :slug")
@Table(name = "poll")
public class PollEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "question")
    private String question;

    @Column(name = "slug")
    private String slug;

    @JoinColumn(name = "poll_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PollOptionEntity> options = Collections.emptySet();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Set<PollOptionEntity> getOptions() {
        return options;
    }

    public void setOptions(Set<PollOptionEntity> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof PollEntity other)) return false;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PollEntity{id=%s}".formatted(this.id);
    }
}

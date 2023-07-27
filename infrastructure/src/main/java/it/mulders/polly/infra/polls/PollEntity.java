package it.mulders.polly.infra.polls;

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
import java.util.Collection;
import java.util.UUID;

@Entity
@NamedQuery(name = "Poll.findBySlug", query = "select p from PollEntity p where p.slug = :slug")
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
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<PollOptionEntity> options;

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

    public Collection<PollOptionEntity> getOptions() {
        return options;
    }

    public void setOptions(Collection<PollOptionEntity> options) {
        this.options = options;
    }
}

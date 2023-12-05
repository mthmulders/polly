package it.mulders.polly.infra.votes;

import it.mulders.polly.infra.PollRelatableEntity;
import it.mulders.polly.infra.polls.PollEntity;
import it.mulders.polly.infra.polls.PollOptionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "vote")
public class VoteEntity implements PollRelatableEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private PollEntity poll;

    @ManyToOne(fetch = FetchType.EAGER)
    private PollOptionEntity option;

    @OneToOne
    private BallotEntity ballot;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PollEntity getPoll() {
        return poll;
    }

    public void setPoll(PollEntity poll) {
        this.poll = poll;
    }

    public PollOptionEntity getOption() {
        return option;
    }

    public void setOption(PollOptionEntity option) {
        this.option = option;
    }

    public BallotEntity getBallot() {
        return ballot;
    }

    public void setBallot(BallotEntity ballot) {
        this.ballot = ballot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof VoteEntity other)) return false;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "VoteEntity{id=%s}".formatted(this.id);
    }
}

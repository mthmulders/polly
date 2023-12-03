package it.mulders.polly.infra.votes;

import it.mulders.polly.infra.PollRelatableEntity;
import it.mulders.polly.infra.polls.PollEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@NamedQuery(name = "Ballot.findByTicketId", query = "select b from BallotEntity b where b.ticketId = :ticketId")
@NamedQuery(
        name = "Ballot.findByPollAndClientIdentifier",
        query =
                "select b from BallotEntity b where b.poll.slug = :poll_slug and b.clientIdentifier = :clientIdentifier")
@Table(name = "ballot")
public class BallotEntity implements PollRelatableEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_identifier")
    private String clientIdentifier;

    @Column(name = "ticket_id")
    private String ticketId;

    @ManyToOne(fetch = FetchType.EAGER)
    private PollEntity poll;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public PollEntity getPoll() {
        return poll;
    }

    public void setPoll(PollEntity poll) {
        this.poll = poll;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof BallotEntity other)) return false;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "BallotEntity{id=%s}".formatted(this.id);
    }
}

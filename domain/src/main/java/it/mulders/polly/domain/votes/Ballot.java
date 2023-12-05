package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The <strong>Ballot</strong> entity represents the right for an individual to cast one (1) vote in a {@link Poll}.
 */
public class Ballot {
    private final String clientIdentifier;
    private final String ticketId;

    protected OffsetDateTime usedAt = null;

    public Ballot(String clientIdentifier, String ticketId) {
        Objects.requireNonNull(clientIdentifier, "Client identifier must not be null");
        Objects.requireNonNull(ticketId, "Ticket ID must not be null");
        this.clientIdentifier = clientIdentifier;
        this.ticketId = ticketId;
    }

    public void markAsUsed() {
        this.usedAt = OffsetDateTime.now();
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public String getTicketId() {
        return ticketId;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Ballot other)) return false;

        return Objects.equals(this.clientIdentifier, other.clientIdentifier) &&
                Objects.equals(this.ticketId, other.ticketId);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(clientIdentifier, ticketId);
    }

    public OffsetDateTime getUsedAt() {
        return usedAt;
    }
}

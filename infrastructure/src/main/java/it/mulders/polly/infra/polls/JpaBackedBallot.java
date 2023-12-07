package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.votes.Ballot;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Adapter for {@link Ballot} to work around the immutability of collections of related entities and remember the database identity.
 */
class JpaBackedBallot extends Ballot {
    private final UUID id;

    public JpaBackedBallot(UUID id, String clientIdentifier, String ticketId, OffsetDateTime usedAt) {
        super(clientIdentifier, ticketId);
        this.id = id;
        this.usedAt = usedAt;
    }

    public UUID getId() {
        return id;
    }
}

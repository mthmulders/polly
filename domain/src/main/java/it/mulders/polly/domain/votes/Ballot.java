package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import java.util.Objects;

/**
 * The <strong>Ballot</strong> entity represents the right for an individual to cast one (1) vote in a {@link Poll}.
 */
public record Ballot(Poll poll, String clientIdentifier, String ticketId) {
    public Ballot {
        Objects.requireNonNull(poll, "Poll must not be null");
        Objects.requireNonNull(clientIdentifier, "Client identifier must not be null");
        Objects.requireNonNull(ticketId, "Ticket ID must not be null");
    }
}

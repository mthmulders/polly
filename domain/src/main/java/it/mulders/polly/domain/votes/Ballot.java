package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import java.util.Objects;

public record Ballot(Poll poll, String ticketId) {
    public Ballot {
        Objects.requireNonNull(poll, "Poll must not be null");
        Objects.requireNonNull(ticketId, "TicketId must not be null");
    }
}

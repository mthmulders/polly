package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import java.util.Objects;

/**
 * The <strong>Vote</strong> data object represents a vote cast in a {@link Poll}.
 */
public record Vote(Poll poll, Ballot ballot, Option option) {
    public Vote {
        Objects.requireNonNull(poll, "Poll must not be null");
        Objects.requireNonNull(ballot, "Ballot must not be null");
        Objects.requireNonNull(option, "Option must not be null");

        if (!ballot.getPoll().equals(poll)) {
            throw new IllegalArgumentException("Ballot belongs to a different poll");
        }
        if (!poll.getOptions().contains(option)) {
            throw new IllegalArgumentException("Option belongs to a different poll");
        }
    }
}

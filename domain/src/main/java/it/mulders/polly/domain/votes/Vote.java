package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import java.util.Objects;

public record Vote(Poll poll, Ballot ballot, Option option) {
    public Vote {
        Objects.requireNonNull(poll, "Poll must not be null");
        Objects.requireNonNull(ballot, "Ballot must not be null");
        Objects.requireNonNull(option, "Option must not be null");

        if (!ballot.poll().equals(poll)) {
            throw new IllegalArgumentException("Ballot belongs to a different poll");
        }
        if (!poll.getOptions().contains(option)) {
            throw new IllegalArgumentException("Option belongs to a different poll");
        }
    }
}

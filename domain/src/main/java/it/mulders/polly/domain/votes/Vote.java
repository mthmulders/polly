package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import java.util.Objects;

/**
 * The <strong>Vote</strong> data object represents a vote cast in a {@link Poll}.
 */
public class Vote {
    private final Ballot ballot;
    private final Option option;

    public Vote(Ballot ballot, Option option) {
        this.ballot = Objects.requireNonNull(ballot, "Ballot must not be null");
        this.option = Objects.requireNonNull(option, "Option must not be null");
    }

    public Ballot getBallot() {
        return ballot;
    }

    public Option getOption() {
        return option;
    }
}

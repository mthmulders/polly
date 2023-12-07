package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;
import java.util.UUID;

/**
 * Adapter for {@link Vote} to work around the immutability of collections of related entities and remember the database identity.
 */
class JpaBackedVote extends Vote {
    private final UUID id;

    public JpaBackedVote(UUID id, Ballot ballot, Option option) {
        super(ballot, option);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}

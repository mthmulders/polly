package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;

import java.util.Set;
import java.util.UUID;

/**
 * Adapter for {@link Poll} to work around the immutability of collections of related entities and remember the database identity.
 */
class JpaBackedPoll extends Poll {
    private final UUID id;

    public JpaBackedPoll(UUID id, String question, String slug, Set<Option> options, Set<Ballot> ballots, Set<Vote> votes) {
        super(question, slug, options, ballots, votes);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}

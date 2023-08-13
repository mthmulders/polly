package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import java.util.Optional;

public interface BallotRepository {
    void store(Ballot ballot);

    Optional<Ballot> findByTicketId(String ticketId);

    Optional<Ballot> findByPollAndClientIdentifier(Poll poll, String clientIdentifier);
}

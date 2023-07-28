package it.mulders.polly.domain.votes;

import java.util.Optional;

public interface BallotRepository {
    void store(Ballot ballot);

    Optional<Ballot> findByTicketId(String ticketId);
}

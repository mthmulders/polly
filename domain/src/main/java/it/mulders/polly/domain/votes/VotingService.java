package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.shared.Result;

/**
 * Describes composite operations on multiple entities from the domain in one action.
 * Such an action is transactional - it either fails as a whole or succeeds as a whole.
 *
 * @implNote It is up to the infrastructure module to provide the transactional boundaries.
 */
public interface VotingService {
    Ballot requestBallotFor(Poll poll, String clientIdentifier);

    Result<Vote> castVote(Poll poll, String ticketId, Integer selectedOption);
}

package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;

public interface VotingService {
    Ballot requestBallotFor(Poll poll, String clientIdentifier);
}

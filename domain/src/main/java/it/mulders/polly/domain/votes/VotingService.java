package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;

public interface VotingService {
    Ballot createBallotFor(Poll poll);
}

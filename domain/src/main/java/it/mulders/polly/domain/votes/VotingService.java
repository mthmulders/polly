package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;

public class VotingService {
    private final BallotRepository ballotRepository;
    public VotingService(BallotRepository ballotRepository) {
        this.ballotRepository = ballotRepository;
    }

    public Ballot createBallotFor(final Poll poll) {
        var ballot = poll.createBallot();
        ballotRepository.store(ballot);
        return ballot;
    }
}

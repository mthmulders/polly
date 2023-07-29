package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;

public class VotingServiceImpl implements VotingService {
    private final BallotRepository ballotRepository;

    public VotingServiceImpl(BallotRepository ballotRepository) {
        this.ballotRepository = ballotRepository;
    }

    @Override
    public Ballot createBallotFor(final Poll poll) {
        var ballot = poll.createBallot();
        ballotRepository.store(ballot);
        return ballot;
    }
}

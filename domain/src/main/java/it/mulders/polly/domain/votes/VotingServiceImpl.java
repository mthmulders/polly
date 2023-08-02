package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;

public class VotingServiceImpl implements VotingService {
    private final BallotRepository ballotRepository;

    public VotingServiceImpl(BallotRepository ballotRepository) {
        this.ballotRepository = ballotRepository;
    }

    @Override
    public Ballot requestBallotFor(final Poll poll, final String clientIdentifier) {
        return ballotRepository
                .findByPollAndClientIdentifier(poll, clientIdentifier)
                .orElseGet(() -> createBallotFor(poll, clientIdentifier));
    }

    private Ballot createBallotFor(final Poll poll, final String clientIdentifier) {
        var ballot = poll.requestBallot(clientIdentifier);
        ballotRepository.store(ballot);
        return ballot;
    }
}

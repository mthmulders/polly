package it.mulders.polly.infra.votes;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.domain.votes.VotingServiceImpl;
import jakarta.transaction.Transactional;

/**
 * Thin layer to make the {@link VotingServiceImpl} behave transactional correct <strong>and</strong> inject
 * the dependencies it needs.
 */
public class TransactionalVotingService extends VotingServiceImpl {
    public TransactionalVotingService(BallotRepository ballotRepository) {
        super(ballotRepository);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Ballot requestBallotFor(Poll poll, String clientIdentifier) {
        return super.requestBallotFor(poll, clientIdentifier);
    }
}

package it.mulders.polly.infra.votes;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.shared.Result;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;
import it.mulders.polly.domain.votes.VotingServiceImpl;
import jakarta.transaction.Transactional;

/**
 * Thin layer to make the {@link VotingServiceImpl} behave transactional correct <strong>and</strong> inject
 * the dependencies it needs.
 */
public class TransactionalVotingService extends VotingServiceImpl {
    public TransactionalVotingService(PollRepository pollRepository) {
        super(pollRepository);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Ballot requestBallotFor(Poll poll, String clientIdentifier) {
        return super.requestBallotFor(poll, clientIdentifier);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Result<Vote> castVote(Poll poll, String ticketId, Integer selectedOption) {
        return super.castVote(poll, ticketId, selectedOption);
    }
}

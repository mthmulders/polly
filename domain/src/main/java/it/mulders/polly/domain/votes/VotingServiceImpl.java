package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.shared.Result;

public class VotingServiceImpl implements VotingService {
    private final PollRepository pollRepository;

    public VotingServiceImpl(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public Ballot requestBallotFor(final Poll poll, final String clientIdentifier) {
        return poll.getBallots().stream()
                .filter(ballot -> clientIdentifier.equals(ballot.getClientIdentifier()))
                .findAny()
                .orElseGet(() -> createBallotFor(poll, clientIdentifier));
    }

    @Override
    public Result<Vote> castVote(Poll poll, String ticketId, Integer selectedOption) {
        try {
            var option = poll.selectOption(selectedOption)
                    .orElseThrow(() -> new NonExistingOptionException(poll.getSlug(), selectedOption));

            var ballot = poll.findBallotByTicketId(ticketId)
                    .orElseThrow(() -> new NonExistingBallotException(poll.getSlug(), ticketId));

            return registerVote(poll, option, ballot);
        } catch (NonExistingOptionException | NonExistingBallotException e) {
            return Result.of(e);
        }
    }

    private Result<Vote> registerVote(Poll poll, Option option, Ballot ballot) {
        if (poll.isBallotUsed(ballot)) {
            return Result.of(new IllegalStateException("This ballot has already been used in this poll"));
        }

        var vote = new Vote(ballot, option);
        poll.registerVote(vote);

        pollRepository.store(poll);

        return Result.of(vote);
    }

    private Ballot createBallotFor(final Poll poll, final String clientIdentifier) {
        var ballot = poll.requestBallot(clientIdentifier);
        pollRepository.store(poll);
        return ballot;
    }
}

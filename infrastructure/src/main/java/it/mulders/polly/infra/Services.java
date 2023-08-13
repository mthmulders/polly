package it.mulders.polly.infra;

import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.domain.votes.VotingService;
import it.mulders.polly.infra.votes.TransactionalVotingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Services {
    @ApplicationScoped
    @Produces
    public VotingService votingService(BallotRepository ballotRepository) {
        return new TransactionalVotingService(ballotRepository);
    }
}

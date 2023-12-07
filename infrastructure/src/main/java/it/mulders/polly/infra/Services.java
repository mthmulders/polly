package it.mulders.polly.infra;

import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.votes.VotingService;
import it.mulders.polly.infra.votes.TransactionalVotingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Services {
    @ApplicationScoped
    @Produces
    public VotingService votingService(PollRepository pollRepository) {
        return new TransactionalVotingService(pollRepository);
    }
}

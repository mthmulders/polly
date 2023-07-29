package it.mulders.polly.infra;

import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.infra.polls.JpaPollRepository;
import it.mulders.polly.infra.polls.PollMapper;
import it.mulders.polly.infra.votes.BallotMapper;
import it.mulders.polly.infra.votes.JpaBallotRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class Repositories {
    @PersistenceContext
    private EntityManager entityManager;

    @ApplicationScoped
    @Produces
    public PollRepository pollRepository(PollMapper pollMapper) {
        return new JpaPollRepository(entityManager, pollMapper);
    }

    @ApplicationScoped
    @Produces
    public BallotRepository ballotRepository(BallotMapper ballotMapper) {
        return new JpaBallotRepository(entityManager, ballotMapper);
    }
}

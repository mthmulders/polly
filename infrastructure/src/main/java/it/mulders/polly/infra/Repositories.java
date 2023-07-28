package it.mulders.polly.infra;

import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.infra.polls.JpaPollRepository;
import it.mulders.polly.infra.polls.PollMapper;
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
    public PollRepository pollRepository(final PollMapper pollMapper) {
        return new JpaPollRepository(entityManager, pollMapper);
    }
}

package it.mulders.polly.infra;

import it.mulders.polly.domain.polls.PollInstanceRepository;
import it.mulders.polly.infra.polls.JpaPollInstanceRepository;
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
    public PollInstanceRepository pollInstanceRepository(final PollMapper pollMapper) {
        return new JpaPollInstanceRepository(entityManager, pollMapper);
    }
}

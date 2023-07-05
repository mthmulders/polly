package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.PollInstance;
import it.mulders.polly.domain.polls.PollInstanceRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class JpaPollInstanceRepository implements PollInstanceRepository {
    private final EntityManager em;
    private final PollMapper pollMapper;

    public JpaPollInstanceRepository(EntityManager em, PollMapper pollMapper) {
        this.em = em;
        this.pollMapper = pollMapper;
    }

    @Override
    public Optional<PollInstance> findByPollSlugAndInstanceSlug(String pollSlug, String pollInstanceSlug) {
        return em.createNamedQuery("PollInstance.findBySlugs", PollInstanceEntity.class)
                .setParameter("instance_slug", pollInstanceSlug)
                .setParameter("poll_slug", pollSlug)
                .getResultStream()
                .findAny()
                .map(pollMapper::pollInstanceEntityToPollInstance);
    }
}

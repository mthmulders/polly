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
        return em.createNamedQuery("Poll.findBySlug", PollEntity.class)
                .setParameter("slug", pollSlug)
                .getResultStream()
                .findAny()
                .flatMap(poll -> findInstanceBySlug(poll, pollInstanceSlug));
    }

    private Optional<PollInstance> findInstanceBySlug(PollEntity poll, String pollInstanceSlug) {
        return poll.getInstances().stream()
                .filter(instance -> pollInstanceSlug.equals(instance.getSlug()))
                .map(instance -> new PollInstance(pollMapper.pollEntityToPoll(poll), pollInstanceSlug))
                .findAny();
    }
}

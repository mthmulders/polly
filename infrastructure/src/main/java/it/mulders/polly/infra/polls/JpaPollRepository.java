package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class JpaPollRepository implements PollRepository {
    private final EntityManager em;
    private final PollMapper pollMapper;

    public JpaPollRepository(EntityManager em, PollMapper pollMapper) {
        this.em = em;
        this.pollMapper = pollMapper;
    }

    @Override
    public Optional<Poll> findBySlug(String slug) {
        return em.createNamedQuery("Poll.findBySlug", PollEntity.class)
                .setParameter("slug", slug)
                .getResultStream()
                .findAny()
                .map(pollMapper::pollEntityToPoll);
    }
}

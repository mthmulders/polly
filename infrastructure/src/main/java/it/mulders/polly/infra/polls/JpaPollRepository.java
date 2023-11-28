package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
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
        try {
            var entity = em.createNamedQuery("Poll.findBySlug", PollEntity.class)
                    .setParameter("slug", slug)
                    .getSingleResult();
            return Optional.of(pollMapper.pollEntityToPoll(entity));
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    @Transactional(Transactional.TxType.MANDATORY)
    @Override
    public void store(Poll poll) {
        var entity = pollMapper.pollToPollEntity(poll);

        if (poll instanceof JpaBackedPoll) {
            entity.getRelatedEntities().forEach(related -> related.setPoll(entity));
            entity.getRelatedEntities().forEach(related -> {
                if (related.getId() == null) {
                    em.persist(related);
                } else {
                    em.merge(related);
                }
            });

            em.merge(entity);
        } else {
            em.persist(entity);
        }
    }
}

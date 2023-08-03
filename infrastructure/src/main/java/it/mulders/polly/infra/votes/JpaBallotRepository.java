package it.mulders.polly.infra.votes;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.infra.polls.PollEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.RollbackException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JpaBallotRepository implements BallotRepository {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final EntityManager em;
    private final BallotMapper ballotMapper;

    public JpaBallotRepository(EntityManager em, BallotMapper ballotMapper) {
        this.em = em;
        this.ballotMapper = ballotMapper;
    }

    private PollEntity findExistingPoll(String slug) {
        return em.createNamedQuery("Poll.findBySlug", PollEntity.class)
                .setParameter("slug", slug)
                .getSingleResult();
    }

    @Transactional(Transactional.TxType.MANDATORY)
    @Override
    public void store(Ballot ballot) {
        var entity = ballotMapper.ballotToBallotEntity(ballot);

        // Lookup the poll entity and link this ballot to it.
        var poll = findExistingPoll(ballot.poll().getSlug());
        entity.setPoll(poll);

        em.persist(entity);
    }

    @Override
    public Optional<Ballot> findByTicketId(String ticketId) {
        try {
            var entity = em.createNamedQuery("Ballot.findByTicketId", BallotEntity.class)
                    .setParameter("ticketId", ticketId)
                    .getSingleResult();
            return Optional.of(ballotMapper.ballotEntityToBallot(entity));
        } catch (NoResultException nre) {
            return Optional.empty();
        } catch (NonUniqueResultException nre) {
            var msg = "There is more than one ballot for ticket [%s]".formatted(ticketId);
            logger.log(Level.SEVERE, nre, () -> msg);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public Optional<Ballot> findByPollAndClientIdentifier(Poll poll, String clientIdentifier) {
        var pollSlug = poll.getSlug();
        try {
            var pollEntity = findExistingPoll(pollSlug);
            var entity = em.createNamedQuery("Ballot.findByPollAndClientIdentifier", BallotEntity.class)
                    .setParameter("poll", pollEntity)
                    .setParameter("clientIdentifier", clientIdentifier)
                    .getSingleResult();
            return Optional.of(ballotMapper.ballotEntityToBallot(entity));
        } catch (NoResultException nre) {
            return Optional.empty();
        } catch (NonUniqueResultException nre) {
            var msg =
                    "There is more than one ballot for poll [%s] and client [%s]".formatted(pollSlug, clientIdentifier);
            logger.log(Level.SEVERE, nre, () -> msg);
            throw new IllegalStateException(msg);
        }
    }
}

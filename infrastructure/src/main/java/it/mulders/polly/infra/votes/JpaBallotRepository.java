package it.mulders.polly.infra.votes;

import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.infra.polls.PollEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    @Transactional(Transactional.TxType.MANDATORY)
    @Override
    public void store(Ballot ballot) {
        var entity = ballotMapper.ballotToBallotEntity(ballot);

        // Lookup the poll entity and link this ballot to it.
        entity.setPoll(em.createNamedQuery("Poll.findBySlug", PollEntity.class)
                .setParameter("slug", ballot.poll().getSlug())
                .getSingleResult());

        try {
            em.persist(entity);
        } catch (RollbackException re) {
            var cause = re.getCause();
            logger.log(Level.SEVERE, cause, () -> "Failed to store ballot [%s] for poll [%s]"
                    .formatted(ballot.ticketId(), ballot.poll().getSlug()));
            throw re;
        }
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
        }
    }
}

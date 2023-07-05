package it.mulders.polly.infra.database;

import it.mulders.polly.domain.shared.Result;
import jakarta.persistence.EntityManager;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uses the transactional capabilities of the Jakarta Persistence API (JPA) rather than the Jakarta Transaction API.
 */
public class JpaTransactionSupport extends TransactionSupport {
    private static final Logger logger = Logger.getLogger(JpaTransactionSupport.class.getName());

    private final EntityManager entityManager;

    public JpaTransactionSupport(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public <T> Result<T> runTransactional(final Supplier<T> action) {
        var transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            var result = action.get();
            transaction.commit();
            return Result.of(result);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Transaction action threw an exception", t);
            transaction.rollback();
            return Result.of(t);
        }
    }
}

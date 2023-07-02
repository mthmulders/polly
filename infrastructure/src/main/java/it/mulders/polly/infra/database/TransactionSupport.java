package it.mulders.polly.infra.database;

import it.mulders.polly.domain.shared.Result;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class TransactionSupport {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Resource
    private UserTransaction userTransaction;

    public TransactionSupport() {}

    protected TransactionSupport(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    public <T> Result<T> runTransactional(final Runnable action) {
        return this.runTransactional(() -> {
            action.run();
            return null;
        });
    }

    public <T> Result<T> runTransactional(final Supplier<T> action) {
        logger.fine("Starting transaction.");
        try {
            userTransaction.begin();
        } catch (SystemException se) {
            logger.log(
                    Level.SEVERE,
                    "There is already a transaction running and the Transaction Manager does not allow nested transactions.",
                    se);
            return Result.of(new TechnicalTransactionException());
        } catch (NotSupportedException nse) {
            logger.log(Level.SEVERE, "The transaction manager encountered an unexpected error condition.", nse);
            return Result.of(new TechnicalTransactionException());
        }

        try {
            logger.fine("Running transaction.");
            final T result;
            try {
                result = action.get();
            } catch (final Exception e) {
                logger.log(Level.SEVERE, "Error running transaction", e);
                userTransaction.rollback();
                return Result.of(e);
            }

            logger.fine("Committing transaction.");
            userTransaction.commit();
            logger.fine("Transaction committed successfully.");
            return Result.of(result);
        } catch (SystemException se) {
            logger.log(Level.SEVERE, "The transaction manager encountered an unexpected error condition.", se);
            return Result.of(new TechnicalTransactionException());
        } catch (RollbackException re) {
            logger.log(Level.SEVERE, "The transaction has been rolled back rather than committed.", re);
            return Result.of(new TransactionRolledBackException());
        } catch (HeuristicMixedException hme) {
            logger.log(
                    Level.SEVERE,
                    "A heuristic decision was made that rolled back some relevant updates and committed others.",
                    hme);
            return Result.of(new TransactionRolledBackException());
        } catch (HeuristicRollbackException hre) {
            logger.log(Level.SEVERE, "A heuristic decision was made that rolled back all relevant updates.", hre);
            return Result.of(new TransactionRolledBackException());
        } catch (SecurityException se) {
            logger.log(Level.SEVERE, "The current thread is not allowed to commit the transaction.", se);
            return Result.of(new TransactionRolledBackException());
        }
    }
}

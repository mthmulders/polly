package it.mulders.polly.infra.database;

import it.mulders.polly.domain.shared.Result;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.util.concurrent.atomic.AtomicBoolean;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TransactionSupportTest implements WithAssertions {
    static class DummyTransaction implements UserTransaction {
        private boolean isRolledBack;
        private boolean isCommitted;

        public DummyTransaction() {}

        @Override
        public void begin() throws NotSupportedException, SystemException {}

        @Override
        public void commit()
                throws SystemException, RollbackException, SecurityException, IllegalStateException,
                        HeuristicMixedException, HeuristicRollbackException {
            isCommitted = true;
        }

        @Override
        public void rollback() throws IllegalStateException, SecurityException {
            isRolledBack = true;
        }

        @Override
        public void setRollbackOnly() throws IllegalStateException {}

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public void setTransactionTimeout(int seconds) {}
    }

    @Test
    void failure_starting_transaction_through_SystemException_should_fail_return_failure() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void begin() throws SystemException {
                throw new SystemException("Woopsadaisy");
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isInstanceOf(TechnicalTransactionException.class);
    }

    @Test
    void failure_starting_transaction_through_NotSupportedException_should_fail_return_failure() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void begin() throws NotSupportedException {
                throw new NotSupportedException("Woopsadaisy");
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isInstanceOf(TechnicalTransactionException.class);
    }

    @Test
    void should_invoke_runnable() {
        var support = new TransactionSupport(new DummyTransaction());
        var result = new AtomicBoolean(false);

        support.runTransactional(() -> result.set(true));

        assertThat(result.get()).isTrue();
    }

    @Test
    void after_run_should_return_success() {
        var support = new TransactionSupport(new DummyTransaction());

        var result = support.runTransactional(() -> {});

        assertThat(result).isInstanceOf(Result.Success.class);
    }

    @Test
    void after_run_should_commit() {
        var transaction = new DummyTransaction();
        var support = new TransactionSupport(transaction);

        support.runTransactional(() -> {});

        assertThat(transaction.isCommitted).isTrue();
    }

    @Test
    void after_failure_should_return_Throwable() {
        var throwable = new RuntimeException("Woopsadaisy");
        var support = new TransactionSupport(new DummyTransaction());

        var result = support.runTransactional(() -> {
            throw throwable;
        });

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isEqualTo(throwable);
    }

    @Test
    void after_failure_should_rollback_transaction() {
        var transaction = new DummyTransaction();

        new TransactionSupport(transaction).runTransactional(() -> {
            throw new RuntimeException("Woopsadaisy");
        });

        assertThat(transaction.isRolledBack).isTrue();
    }

    @Test
    void after_SystemException_should_rollback_transaction() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void commit() throws SystemException {
                throw new SystemException("Woopsadaisy");
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isInstanceOf(TechnicalTransactionException.class);
    }

    @Test
    void after_HeuristicMixedException_should_rollback_transaction() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void commit() throws HeuristicMixedException {
                throw new HeuristicMixedException("Woopsadaisy");
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isInstanceOf(TransactionRolledBackException.class);
    }

    @Test
    void after_HeuristicRollbackException_should_rollback_transaction() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void commit() throws HeuristicRollbackException {
                throw new HeuristicRollbackException("Woopsadaisy");
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isInstanceOf(TransactionRolledBackException.class);
    }

    @Test
    void after_SecurityException_should_rollback_transaction() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void commit() throws SecurityException {
                throw new SecurityException("Woopsadaisy");
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result)
                .isInstanceOf(Result.Failure.class)
                .extracting(Result::getCause)
                .isInstanceOf(TransactionRolledBackException.class);
    }

    @Test
    void after_rollback_should_return_failure() {
        var support = new TransactionSupport(new DummyTransaction() {
            @Override
            public void commit() throws RollbackException, SecurityException, IllegalStateException {
                throw new RollbackException();
            }
        });

        var result = support.runTransactional(() -> {});

        assertThat(result).isInstanceOf(Result.Failure.class);
    }
}

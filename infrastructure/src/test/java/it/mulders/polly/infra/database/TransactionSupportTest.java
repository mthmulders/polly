package it.mulders.polly.infra.database;

import it.mulders.polly.domain.shared.Result;
import jakarta.transaction.RollbackException;
import jakarta.transaction.UserTransaction;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TransactionSupportTest implements WithAssertions {
    static class DummyTransaction implements UserTransaction {
        private final boolean shouldRollback;
        private boolean isRolledBack;
        private boolean isCommitted;

        public DummyTransaction(final boolean shouldRollback) {
            this.shouldRollback = shouldRollback;
        }

        @Override
        public void begin() {}

        @Override
        public void commit() throws RollbackException, SecurityException, IllegalStateException {
            if (shouldRollback) {
                throw new RollbackException();
            } else {
                isCommitted = true;
            }
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
    ;

    @Test
    void after_run_should_return_success() {
        var support = new TransactionSupport(new DummyTransaction(false));

        var result = support.runTransactional(() -> {});

        assertThat(result).isInstanceOf(Result.Success.class);
    }

    @Test
    void after_run_should_commit() {
        var transaction = new DummyTransaction(false);
        var support = new TransactionSupport(transaction);

        support.runTransactional(() -> {});

        assertThat(transaction.isCommitted).isTrue();
    }

    @Test
    void after_failure_should_return_Throwable() {
        var throwable = new RuntimeException("Woopsadaisy");
        var support = new TransactionSupport(new DummyTransaction(false));

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
        var throwable = new RuntimeException("Woopsadaisy");
        var transaction = new DummyTransaction(false);

        new TransactionSupport(transaction).runTransactional(() -> {
            throw throwable;
        });

        assertThat(transaction.isRolledBack).isTrue();
    }

    @Test
    void after_rollback_should_return_failure() {
        var support = new TransactionSupport(new DummyTransaction(true));

        var result = support.runTransactional(() -> {});

        assertThat(result).isInstanceOf(Result.Failure.class);
    }
}

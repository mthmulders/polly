package it.mulders.polly.domain.shared;

import it.mulders.polly.domain.polls.Poll;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Modelled after Scala's Try class, this is a container for either the outcome of a successful computation or a failure.
 * @param <T> The type of the computation result.
 */
public sealed interface Result<T> {
    T getValue();

    <R> Result<R> map(Function<T, R> converter);

    <R> Result<R> flatMap(Function<T, Result<R>> converter);

    Throwable getCause();

    T getOrElse(T other);

    default T getOrElseGet(Supplier<T> supplier) {
        return getOrElse(supplier.get());
    }

    T getOrElseConvertCause(Function<Throwable, T> converter);

    static <T> Result<T> success() {
        return new Success<>();
    }

    static <T> Result<T> of(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> of(Throwable cause) {
        return new Failure<>(cause);
    }

    final class Success<T> implements Result<T> {
        private final T value;

        public Success() {
            this.value = null;
        }

        public Success(final T value) {
            this.value = value;
        }

        @Override
        public T getValue() {
            return this.value;
        }

        @Override
        public <R> Result<R> map(Function<T, R> converter) {
            return new Success<>(converter.apply(this.value));
        }

        @Override
        public <R> Result<R> flatMap(Function<T, Result<R>> converter) {
            return converter.apply(value);
        }

        @Override
        public Throwable getCause() {
            throw new IllegalStateException("This success value has no throwable cause");
        }

        @Override
        public T getOrElse(T other) {
            return this.value;
        }

        @Override
        public T getOrElseConvertCause(Function<Throwable, T> converter) {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Result.Success<?> other)) return false;

            return (this.value == null && other.value == null) || (this.value != null && this.value.equals(other.value));
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.value);
        }
    }

    @SuppressWarnings("java:S6206") // Do not suggest to turn this into a Record
    final class Failure<T> implements Result<T> {
        private final Throwable cause;

        public Failure(final Throwable cause) {
            this.cause = Objects.requireNonNull(cause, "Cause must not be null");
        }

        @Override
        public T getValue() {
            throw new IllegalStateException(cause);
        }

        @Override
        public <R> Result<R> map(Function<T, R> converter) {
            return (Result<R>) this;
        }

        @Override
        public <R> Result<R> flatMap(Function<T, Result<R>> converter) {
            return (Result<R>) this;
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public T getOrElse(T other) {
            return other;
        }

        @Override
        public T getOrElseConvertCause(Function<Throwable, T> converter) {
            return converter.apply(cause);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Result.Failure<?> other)) return false;

            return this.cause.equals(other.cause);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.cause);
        }
    }
}

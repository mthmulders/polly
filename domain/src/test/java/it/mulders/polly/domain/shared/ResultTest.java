package it.mulders.polly.domain.shared;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ResultTest implements WithAssertions {
    @Test
    void resultSuccess_should_be_Success() {
        assertThat(Result.success()).isInstanceOf(Result.Success.class);
    }

    @Test
    void resultOfValue_should_be_Success() {
        assertThat(Result.of(42)).isInstanceOf(Result.Success.class);
    }

    @Test
    void resultOfThrowable_should_be_Failure() {
        assertThat(Result.of(new NullPointerException())).isInstanceOf(Result.Failure.class);
    }

    @Nested
    class Success {
        private final Result.Success<Integer> success = new Result.Success<>(42);
        @Test
        void should_map_to_different_type() {
            var result = success.map(String::valueOf);
            assertThat(result.getValue()).isEqualTo("42");
        }
        @Test
        void should_flatMap_to_different_type() {
            var result = success.flatMap(x -> new Result.Success<>(String.valueOf(x)));
            assertThat(result.getValue()).isEqualTo("42");
        }

        @Test
        void getOrElse_should_return_value() {
            assertThat(success.getOrElse(13)).isEqualTo(42);
        }

        @Test
        void getOrElseGet_should_return_value() {
            assertThat(success.getOrElseGet(() -> 13)).isEqualTo(42);
        }

        @Test
        void should_not_have_cause() {
            assertThatThrownBy(success::getCause).isInstanceOf(IllegalStateException.class);
        }

        @Test
        void getOrElseConvertCause_should_return_value() {
            assertThat(success.getOrElseConvertCause((x) -> 13)).isEqualTo(42);
        }

        @Test
        void can_have_no_value() {
            assertThat(new Result.Success<>().getValue()).isNull();
        }
    }

    @Nested
    class Failure {
        private final Result.Failure<Integer> failure = new Result.Failure<>(new NullPointerException());
        @Test
        void should_not_map_to_different_type() {
            var result = failure.map(String::valueOf);
            assertThat(result.getCause()).isInstanceOf(NullPointerException.class);
        }
        @Test
        void should_not_flatMap_to_different_type() {
            var result = failure.flatMap(x -> new Result.Success<>(String.valueOf(x)));
            assertThat(result.getCause()).isInstanceOf(NullPointerException.class);
        }

        @Test
        void getOrElse_should_return_other_value() {
            assertThat(failure.getOrElse(13)).isEqualTo(13);
        }

        @Test
        void getOrElseGet_forSuccess_should_return_value() {
            assertThat(failure.getOrElseGet(() -> 13)).isEqualTo(13);
        }

        @Test
        void should_have_cause() {
            assertThat(failure.getCause()).isInstanceOf(NullPointerException.class);
        }

        @Test
        void getOrElseConvertCause_should_return_value() {
            assertThat(failure.getOrElseConvertCause((x) -> 13)).isEqualTo(13);
        }

        @Test
        void has_no_value() {
            assertThatThrownBy(() -> failure.getValue()).isInstanceOf(IllegalStateException.class);
        }
    }
}

package it.mulders.polly.domain.polls;

import java.util.Objects;

/**
 * A possible answer to a {@link Poll}.
 */
public record Option(String displayValue) {
    public Option {
        Objects.requireNonNull(displayValue, "Display value must not be null");
    }
}

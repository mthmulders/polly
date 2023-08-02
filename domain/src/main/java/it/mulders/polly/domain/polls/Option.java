package it.mulders.polly.domain.polls;

import java.util.Objects;

/**
 * A possible answer to a {@link Poll}.
 */
public record Option(Integer optionValue, String displayValue) {
    public Option {
        Objects.requireNonNull(optionValue, "Option value must not be null");
        Objects.requireNonNull(displayValue, "Display value must not be null");
    }
}

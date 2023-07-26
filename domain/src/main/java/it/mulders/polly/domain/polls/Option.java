package it.mulders.polly.domain.polls;

import java.util.Objects;

/**
 * A possible answer to a {@link Poll}.
 */
public class Option {
    // TODO Is this an entity in itself or "just" a value object?

    private final String displayValue;

    public Option(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Option other)) return false;

        return this.displayValue.equals(other.displayValue);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(displayValue);
    }
}

package it.mulders.polly.domain.polls;

import java.util.Objects;

/**
 * A possible answer to a {@link Poll}.
 */
public class Option {
    private final Integer optionValue;
    private final String displayValue;

    public Option(Integer optionValue, String displayValue) {
        this.optionValue = Objects.requireNonNull(optionValue, "Option value must not be null");
        this.displayValue = Objects.requireNonNull(displayValue, "Display value must not be null");
    }

    public Integer getOptionValue() {
        return optionValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Option other)) return false;

        return Objects.equals(this.optionValue, other.optionValue)
                && Objects.equals(this.displayValue, other.displayValue);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(optionValue, displayValue);
    }
}

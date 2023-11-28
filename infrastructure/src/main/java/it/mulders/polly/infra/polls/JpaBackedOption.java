package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Option;
import java.util.UUID;

/**
 * Adapter for {@link Option} to work around the immutability of collections of related entities and remember the database identity.
 */
public class JpaBackedOption extends Option {
    private final UUID id;

    public JpaBackedOption(UUID id, Integer optionValue, String displayValue) {
        super(optionValue, displayValue);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}

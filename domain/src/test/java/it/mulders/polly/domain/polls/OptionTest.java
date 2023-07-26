package it.mulders.polly.domain.polls;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OptionTest implements WithAssertions {

    @Test
    void honours_equals_contract() {
        EqualsVerifier.forClass(Option.class)
                .withNonnullFields("displayValue")
                .verify();
    }

}
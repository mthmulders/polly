package it.mulders.polly.domain.polls;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OptionTest implements WithAssertions {
    @Test
    void construct_valid_instance() {
        var option = new Option(1, "Could be an option...");

        assertThat(option.displayValue()).isEqualTo("Could be an option...");
    }

    @Test
    void displayValue_can_not_be_null() {
        assertThatThrownBy(() -> new Option(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Display value");
    }

    @Test
    void optionValue_can_not_be_null() {
        assertThatThrownBy(() -> new Option(null, "Could be an option"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Option value");
    }

    @Test
    void honours_equals_contract() {
        EqualsVerifier.forClass(Option.class)
                .withNonnullFields("optionValue", "displayValue")
                .verify();
    }
}

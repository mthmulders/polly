package it.mulders.polly.domain.polls;

import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollTest implements WithAssertions {
    private final Set<Option> options = Set.of(new Option(1, "I'm good"), new Option(2, "So-so"));

    @Test
    void construct_valid_instance() {
        var poll = new Poll("How are you?", "how-are-you", options);

        assertThat(poll.getQuestion()).isEqualTo("How are you?");
        assertThat(poll.getSlug()).isEqualTo("how-are-you");
    }

    @Test
    void slug_can_not_be_null() {
        assertThatThrownBy(() -> new Poll("How are you?", null, options))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Slug");
    }

    @Test
    void question_can_not_be_null() {
        assertThatThrownBy(() -> new Poll(null, "how-are-you", options))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Question");
    }

    @Test
    void options_can_not_be_null() {
        assertThatThrownBy(() -> new Poll("How are you?", "how-are-you", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Options");
    }

    @Test
    void honours_equals_contract() {
        EqualsVerifier.forClass(Poll.class)
                .withNonnullFields("slug", "question", "options")
                .verify();
    }
}

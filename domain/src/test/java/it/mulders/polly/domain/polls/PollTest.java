package it.mulders.polly.domain.polls;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollTest implements WithAssertions {
    @Test
    void construct_valid_instance() {
        var poll = new Poll("How are you?", "how-are-you");

        assertThat(poll.getQuestion()).isEqualTo("How are you?");
        assertThat(poll.getSlug()).isEqualTo("how-are-you");
    }

    @Test
    void slug_can_not_be_null() {
        assertThatThrownBy(() -> new Poll("How are you?", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Slug");
    }

    @Test
    void question_can_not_be_null() {
        assertThatThrownBy(() -> new Poll(null, "how-are-you"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Question");
    }

    @Test
    void honours_equals_contract() {
        EqualsVerifier.forClass(Poll.class)
                .withNonnullFields("slug", "question")
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
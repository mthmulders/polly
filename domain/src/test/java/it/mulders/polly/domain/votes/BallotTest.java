package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.impl.RandomStringUtils;
import it.mulders.polly.domain.polls.Poll;
import java.util.Collections;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BallotTest implements WithAssertions {
    private final Poll poll = new Poll("How are you?", "how-are-you", Collections.emptySet());

    @Test
    void construct_valid_instance() {
        var ticketId = RandomStringUtils.generateRandomIdentifier(8);
        var ballot = new Ballot(poll, ticketId);

        assertThat(ballot.ticketId()).isEqualTo(ticketId);
        assertThat(ballot.poll()).isEqualTo(poll);
    }

    @Test
    void poll_can_not_be_null() {
        assertThatThrownBy(() -> new Ballot(null, ""))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Poll");
    }

    @Test
    void ticketId_can_not_be_null() {
        assertThatThrownBy(() -> new Ballot(poll, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("TicketId");
    }

    @Test
    void honours_equals_contract() {
        EqualsVerifier.forClass(Ballot.class)
                .withNonnullFields("poll", "ticketId")
                .verify();
    }
}

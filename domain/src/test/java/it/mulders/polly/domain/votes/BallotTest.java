package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.impl.RandomStringUtils;
import it.mulders.polly.domain.polls.Poll;
import java.util.Collections;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BallotTest implements WithAssertions {
    private final String clientIdentifier = UUID.randomUUID().toString();
    private final Poll poll = new Poll("How are you?", "how-are-you", Collections.emptySet());

    @Test
    void construct_valid_instance() {
        var ticketId = RandomStringUtils.generateRandomIdentifier(8);
        var ballot = new Ballot(poll, clientIdentifier, ticketId);

        assertThat(ballot.getPoll()).isEqualTo(poll);
        assertThat(ballot.getClientIdentifier()).isEqualTo(clientIdentifier);
        assertThat(ballot.getTicketId()).isEqualTo(ticketId);
    }

    @Test
    void poll_can_not_be_null() {
        assertThatThrownBy(() -> new Ballot(null, clientIdentifier, ""))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Poll");
    }

    @Test
    void clientIdentifier_can_not_be_null() {
        var ticketId = RandomStringUtils.generateRandomIdentifier(8);
        assertThatThrownBy(() -> new Ballot(poll, null, ticketId))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Client identifier");
    }

    @Test
    void ticketId_can_not_be_null() {
        assertThatThrownBy(() -> new Ballot(poll, clientIdentifier, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Ticket ID");
    }

    @Test
    void honours_equals_contract() {
        EqualsVerifier.forClass(Ballot.class)
                .withNonnullFields("poll", "clientIdentifier", "ticketId")
                .verify();
    }
}

package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.impl.RandomStringUtils;
import it.mulders.polly.domain.polls.Option;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BallotTest implements WithAssertions {
    private final String clientIdentifier = UUID.randomUUID().toString();
    private final Option option1 = new Option(1, "I'm good");
    private final Option option2 = new Option(2, "So-so");

    @Test
    void construct_valid_instance() {
        var ticketId = RandomStringUtils.generateRandomIdentifier(8);
        var ballot = new Ballot(clientIdentifier, ticketId);

        assertThat(ballot.getClientIdentifier()).isEqualTo(clientIdentifier);
        assertThat(ballot.getTicketId()).isEqualTo(ticketId);
    }

    @Test
    void clientIdentifier_can_not_be_null() {
        var ticketId = RandomStringUtils.generateRandomIdentifier(8);
        assertThatThrownBy(() -> new Ballot(null, ticketId))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Client identifier");
    }

    @Test
    void ticketId_can_not_be_null() {
        assertThatThrownBy(() -> new Ballot(clientIdentifier, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Ticket ID");
    }

    @Test
    void honours_equals_contract() {
        var ballot = new Ballot(UUID.randomUUID().toString(), RandomStringUtils.generateRandomIdentifier(8));
        var vote1 = new Vote(ballot, option1);
        var vote2 = new Vote(ballot, option2);

        EqualsVerifier.forClass(Ballot.class)
                .withPrefabValues(Vote.class, vote1, vote2)
                .withNonnullFields("clientIdentifier", "ticketId")
                .withIgnoredFields("usedAt")
                .verify();
    }
}

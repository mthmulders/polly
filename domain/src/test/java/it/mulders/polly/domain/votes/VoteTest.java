package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static it.mulders.polly.domain.impl.RandomStringUtils.generateRandomIdentifier;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VoteTest implements WithAssertions {
    private final Option option1 = new Option(0, "OK");
    private final Option option2 = new Option(1, "So-so");
    private final Poll poll = new Poll("How are you?", "how-are-you", Set.of(option1, option2));
    private final Ballot ballot = new Ballot(poll, UUID.randomUUID().toString(), generateRandomIdentifier(8));

    @Test
    void construct_valid_instance() {
        var vote = new Vote(poll, ballot, option1);

        assertThat(vote.poll()).isEqualTo(poll);
        assertThat(vote.ballot()).isEqualTo(ballot);
        assertThat(vote.option()).isEqualTo(option1);
    }

    @Test
    void poll_can_not_be_null() {
        assertThatThrownBy(() -> new Vote(null, ballot, option1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Poll");
    }

    @Test
    void ballot_can_not_be_null() {
        assertThatThrownBy(() -> new Vote(poll, null, option1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Ballot");
    }

    @Test
    void option_can_not_be_null() {
        assertThatThrownBy(() -> new Vote(poll, ballot, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Option");
    }

    @Test
    void ballot_cannot_belong_to_different_poll() {
        var differentPoll = new Poll("What's up?", "whats-up", Collections.emptySet());

        assertThatThrownBy(() -> new Vote(differentPoll, ballot, option1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ballot")
                .hasMessageContaining("different poll");
    }

    @Test
    void option_cannot_belong_to_different_poll() {
        var differentOption = new Option(2, "Just bad");

        assertThatThrownBy(() -> new Vote(poll, ballot, differentOption))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Option")
                .hasMessageContaining("different poll");
    }
}
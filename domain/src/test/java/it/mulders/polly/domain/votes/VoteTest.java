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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VoteTest implements WithAssertions {
    private final Option option1 = new Option(0, "OK");
    private final Option option2 = new Option(1, "So-so");
    private final Poll poll = new Poll("How are you?", "how-are-you", Set.of(option1, option2));
    private final Ballot ballot = poll.requestBallot(UUID.randomUUID().toString());

    @Test
    void construct_valid_instance() {
        var vote = new Vote(ballot, option1);

        assertThat(vote.getBallot()).isEqualTo(ballot);
        assertThat(vote.getOption()).isEqualTo(option1);
    }


    @Test
    void ballot_can_not_be_null() {
        assertThatThrownBy(() -> new Vote(null, option1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Ballot");
    }

    @Test
    void option_can_not_be_null() {
        assertThatThrownBy(() -> new Vote(ballot, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Option");
    }
}
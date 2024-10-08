package it.mulders.polly.domain.polls;

import it.mulders.polly.domain.impl.RandomStringUtils;
import it.mulders.polly.domain.votes.Vote;
import java.math.BigDecimal;
import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollTest implements WithAssertions {
    private final Option option1 = new Option(1, "I'm good");
    private final Option option2 = new Option(2, "So-so");
    private final Set<Option> options = Set.of(option1, option2);

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
        var poll = new Poll("How are you?", "how-are-you", options);
        var ballot = poll.requestBallot(RandomStringUtils.generateRandomIdentifier(8));
        var vote1 = new Vote(ballot, option1);
        var vote2 = new Vote(ballot, option2);
        EqualsVerifier.forClass(Poll.class)
                .withPrefabValues(Vote.class, vote1, vote2)
                .withNonnullFields("slug", "question", "options")
                .verify();
    }

    @Test
    void find_existing_ballot() {
        var clientIdentifier = "e6PLVAM0jGcPtovnWrvYXJt";
        var poll = new Poll("How are you?", "how-are-you", options);
        var ballot = poll.requestBallot(clientIdentifier);

        var result = poll.findBallotByTicketId(ballot.getTicketId());

        assertThat(result).isPresent().contains(ballot);
    }

    @Test
    void find_non_existing_ballot() {
        var poll = new Poll("How are you?", "how-are-you", options);

        var result = poll.findBallotByTicketId("7FSSRB7L");

        assertThat(result).isEmpty();
    }

    @Nested
    class ReportAboutVoting {
        @Test
        void fifty_fifty() {
            var poll = new Poll("How are you?", "how-are-you", options);
            var ballot = poll.requestBallot(RandomStringUtils.generateRandomIdentifier(8));
            poll.registerVote(new Vote(ballot, option1));
            poll.registerVote(new Vote(ballot, option2));

            var result = poll.calculateVotePercentages();

            assertThat(result).containsEntry(option1, new BigDecimal("50"));
            assertThat(result).containsEntry(option2, new BigDecimal("50"));
        }

        @Test
        void one_to_three() {
            var poll = new Poll("How are you?", "how-are-you", options);
            var ballot = poll.requestBallot(RandomStringUtils.generateRandomIdentifier(8));
            poll.registerVote(new Vote(ballot, option1));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));

            var result = poll.calculateVotePercentages();

            assertThat(result).containsEntry(option1, new BigDecimal("25"));
            assertThat(result).containsEntry(option2, new BigDecimal("75"));
        }

        @Test
        void round_percentage_to_two_decimal_places() {
            var poll = new Poll("How are you?", "how-are-you", options);
            var ballot = poll.requestBallot(RandomStringUtils.generateRandomIdentifier(8));
            poll.registerVote(new Vote(ballot, option1));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));

            var result = poll.calculateVotePercentages();

            assertThat(result).containsEntry(option1, new BigDecimal("14.3"));
            assertThat(result).containsEntry(option2, new BigDecimal("85.7"));
        }

        @Test
        void all_to_one_option() {
            var poll = new Poll("How are you?", "how-are-you", options);
            var ballot = poll.requestBallot(RandomStringUtils.generateRandomIdentifier(8));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));
            poll.registerVote(new Vote(ballot, option2));

            var result = poll.calculateVotePercentages();

            assertThat(result).containsEntry(option1, new BigDecimal("0"));
            assertThat(result).containsEntry(option2, new BigDecimal("100"));
        }
    }
}

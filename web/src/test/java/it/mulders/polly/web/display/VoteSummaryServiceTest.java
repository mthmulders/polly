package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Vote;
import it.mulders.polly.web.display.dto.OptionWithPercentage;
import java.math.BigDecimal;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VoteSummaryServiceTest implements WithAssertions {
    private final VoteSummaryService voteSummaryService = new VoteSummaryService();

    private final Option option1 = new Option(1, "I'm good");
    private final Option option2 = new Option(2, "So-so");
    private final Option option3 = new Option(3, "Not too bad");
    private final Set<Option> options = Set.of(option1, option2, option3);
    private final Poll poll = new Poll("How are you?", "how-are-you", options);

    VoteSummaryServiceTest() {
        var ballot = poll.requestBallot("aaaaaaaa");

        poll.registerVote(new Vote(ballot, option1)); // 1 vote
        poll.registerVote(new Vote(ballot, option2)); // 2 votes
        poll.registerVote(new Vote(ballot, option2));
        poll.registerVote(new Vote(ballot, option3)); // 5 votes
        poll.registerVote(new Vote(ballot, option3));
        poll.registerVote(new Vote(ballot, option3));
        poll.registerVote(new Vote(ballot, option3));
        poll.registerVote(new Vote(ballot, option3));
    }

    @Nested
    class CalculateVotePercentagesTest {
        @Test
        void should_calculate_vote_percentages() {
            var result = voteSummaryService.calculateVotePercentages(poll);

            assertThat(result.size()).isEqualTo(options.size());
            assertThat(result)
                    .containsExactlyInAnyOrder(
                            new OptionWithPercentage(option1.getDisplayValue(), new BigDecimal("12.5")),
                            new OptionWithPercentage(option2.getDisplayValue(), new BigDecimal("25")),
                            new OptionWithPercentage(option3.getDisplayValue(), new BigDecimal("62.5")));
        }

        @Test
        void should_sort_outcome_on_percentage_descending() {
            var result = voteSummaryService.calculateVotePercentages(poll);

            assertThat(result)
                    .map(OptionWithPercentage::optionLabel)
                    .containsSequence(option3.getDisplayValue(), option2.getDisplayValue(), option1.getDisplayValue());
        }
    }

    @Nested
    class CalculateVoteCountTest {
        @Test
        void should_calculate_vote_count() {
            assertThat(voteSummaryService.calculateVoteCount(poll)).isEqualTo(8);
        }
    }
}

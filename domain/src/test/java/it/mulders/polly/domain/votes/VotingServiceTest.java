package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.shared.Result;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VotingServiceTest implements WithAssertions {
    private final InMemoryPollRepository pollRepository = new InMemoryPollRepository();
    private final VotingService votingService = new VotingServiceImpl(pollRepository);

    @DisplayName("Creating ballots")
    @Nested
    class CreatingBallots {

        @Test
        void should_create_ballot_for_poll() {
            var poll = new Poll("How are you?", "should-create-ballot-for-poll", Collections.emptySet());

            var result = votingService.requestBallotFor(poll, UUID.randomUUID().toString());

            assertThat(result).isInstanceOf(Ballot.class);
        }

        @Test
        void should_return_existing_bullet_for_same_clientIdentifier() {
            var clientIdentifier = UUID.randomUUID().toString();
            var poll = new Poll(
                    "How are you?", "should-return-existing-bullet-for-same-clientIdentifier", Collections.emptySet());

            var ballot1 = votingService.requestBallotFor(poll, clientIdentifier);
            var ballot2 = votingService.requestBallotFor(poll, clientIdentifier);

            assertThat(ballot2).isEqualTo(ballot1);
        }

        @Test
        void should_store_ballot() {
            var clientIdentifier = UUID.randomUUID().toString();
            var poll = new Poll("How are you?", "should-store-ballot", Collections.emptySet());

            var result = votingService.requestBallotFor(poll, clientIdentifier);

            assertThat(pollRepository.findBySlug(poll.getSlug()))
                    .isPresent()
                    .hasValueSatisfying(updatedPoll -> {
                        assertThat(updatedPoll.getBallots()).contains(result);
                    });
        }
    }

    @DisplayName("Casting a vote")
    @Nested
    class CastingVotes {
        private final Option option1 = new Option(0, "OK");
        private final Option option2 = new Option(1, "So-so");
        private final Poll poll = new Poll("How are you?", "how-are-you", Set.of(option1, option2));
        private final String clientIdentifier = UUID.randomUUID().toString();
        private final Ballot ballot = poll.requestBallot(clientIdentifier);

        @Test
        void should_mark_ballot_as_used() {
            votingService.castVote(poll, ballot.getTicketId(), option1.getOptionValue());

            assertThat(ballot.getUsedAt()).isCloseToUtcNow(within(1, ChronoUnit.SECONDS));
        }

        @Test
        void should_store_vote() {
            var voteCountBefore = poll.getVotes().size();
            var result = votingService.castVote(poll, ballot.getTicketId(), option1.getOptionValue());
            var voteCountAfter = poll.getVotes().size();

            assertThat(voteCountAfter).isGreaterThan(voteCountBefore);
            assertThat(result)
                    .isInstanceOf(Result.Success.class)
                    .extracting(Result::getValue)
                    .satisfies(vote -> {
                        assertThat(poll.getVotes()).contains(vote);
                        assertThat(vote.getOption()).isEqualTo(option1);
                    });
        }

        @Test
        void should_prevent_duplicate_ballot() {
            var result1 = votingService.castVote(poll, ballot.getTicketId(), option1.getOptionValue());
            assertThat(result1).isInstanceOf(Result.Success.class);

            var result2 = votingService.castVote(poll, ballot.getTicketId(), option1.getOptionValue());
            assertThat(result2)
                    .isInstanceOf(Result.Failure.class)
                    .extracting(Result::getCause)
                    .extracting(Throwable::getMessage)
                    .satisfies(message -> assertThat(message).contains("ballot has already been used"));
        }

        @Test
        void should_fail_on_non_existing_ballot() {
            var result = votingService.castVote(poll, UUID.randomUUID().toString(), option1.getOptionValue());

            assertThat(result)
                    .isInstanceOf(Result.Failure.class)
                    .extracting(Result::getCause)
                    .satisfies(cause -> assertThat(cause)
                            .isInstanceOf(NonExistingBallotException.class)
                            .hasMessageContaining("Unknown ballot"));
        }

        @Test
        void should_fail_on_non_existing_option() {
            var result = votingService.castVote(poll, UUID.randomUUID().toString(), 3);

            assertThat(result)
                    .isInstanceOf(Result.Failure.class)
                    .extracting(Result::getCause)
                    .satisfies(cause -> assertThat(cause)
                            .isInstanceOf(NonExistingOptionException.class)
                            .hasMessageContaining("Unknown option"));
        }

        @Test
        void ballot_cannot_belong_to_different_poll() {
            var differentPoll = new Poll("How are you?", "how-are-you", Set.of());
            differentPoll.requestBallot(clientIdentifier);
            pollRepository.store(poll);

            var result = votingService.castVote(poll, UUID.randomUUID().toString(), 1);
            assertThat(result)
                    .isInstanceOf(Result.Failure.class)
                    .extracting(Result::getCause)
                    .extracting(Throwable::getMessage)
                    .satisfies(message -> assertThat(message).contains("Unknown ballot"));
        }
    }

    private static class InMemoryPollRepository extends HashSet<Poll> implements PollRepository {

        @Override
        public Optional<Poll> findBySlug(String slug) {
            return stream()
                    .filter(poll -> slug.equals(poll.getSlug()))
                    .findAny();
        }

        @Override
        public void store(Poll poll) {
            add(poll);
        }
    }
}

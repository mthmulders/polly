package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VotingServiceTest implements WithAssertions {
    private final InMemoryBallotRepository ballotRepository = new InMemoryBallotRepository();
    private final VotingService votingService = new VotingServiceImpl(ballotRepository);

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

            assertThat(ballotRepository).contains(result);
        }
    }

    private static class InMemoryBallotRepository extends HashSet<Ballot> implements BallotRepository {

        @Override
        public void store(Ballot ballot) {
            add(ballot);
        }

        @Override
        public Optional<Ballot> findByTicketId(String ticketId) {
            return stream().filter(ballot -> ticketId.equals(ballot.ticketId())).findAny();
        }

        @Override
        public Optional<Ballot> findByPollAndClientIdentifier(Poll poll, String clientIdentifier) {
            return stream()
                    .filter(ballot -> ballot.poll().equals(poll))
                    .filter(ballot -> ballot.clientIdentifier().equals(clientIdentifier))
                    .findAny();
        }
    }
}

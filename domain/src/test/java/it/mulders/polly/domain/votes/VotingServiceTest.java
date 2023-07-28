package it.mulders.polly.domain.votes;

import it.mulders.polly.domain.polls.Poll;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VotingServiceTest implements WithAssertions {
    private final InMemoryBallotRepository ballotRepository = new InMemoryBallotRepository();
    private final VotingService votingService = new VotingService(ballotRepository);

    @Test
    void should_create_ballot_for_poll() {
        var poll = new Poll("How are you?", "how-are-you", Collections.emptySet());

        var result = votingService.createBallotFor(poll);

        assertThat(result).isInstanceOf(Ballot.class);
    }

    @Test
    void should_store_ballot() {
        var poll = new Poll("How are you?", "how-are-you", Collections.emptySet());

        var result = votingService.createBallotFor(poll);

        assertThat(ballotRepository).contains(result);
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
    }
}

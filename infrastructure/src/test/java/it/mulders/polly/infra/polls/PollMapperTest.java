package it.mulders.polly.infra.polls;

import static java.util.Collections.emptySet;
import static java.util.UUID.randomUUID;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.votes.BallotEntity;
import it.mulders.polly.infra.votes.VoteEntity;
import java.util.UUID;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollMapperTest implements WithAssertions {
    private final PollMapper mapper = MapStructHelper.getMapper(PollMapper.class);

    @Nested
    class PollMapping {
        @Test
        void should_copy_primary_key_from_database_retrieved_entity() {
            var primaryKey = randomUUID();
            var input = new JpaBackedPoll(primaryKey, "", "", emptySet(), emptySet(), emptySet());

            var result = mapper.pollToPollEntity(input);

            assertThat(result.getId()).isEqualTo(primaryKey);
        }

        @Test
        void should_copy_primary_key_to_domain_object() {
            var primaryKey = randomUUID();
            var input = new PollEntity();
            input.setId(primaryKey);
            input.setSlug("");
            input.setQuestion("");

            var result = mapper.pollEntityToPoll(input);

            assertThat(result).isInstanceOf(JpaBackedPoll.class).hasFieldOrPropertyWithValue("id", primaryKey);
        }
    }

    @Nested
    class BallotMapping {
        @Test
        void should_copy_primary_key_from_database_retrieved_entity() {
            var primaryKey = randomUUID();
            var input = new JpaBackedBallot(primaryKey, "", "", null);

            var result = mapper.ballotToBallotEntity(input);

            assertThat(result.getId()).isEqualTo(primaryKey);
        }

        @Test
        void should_copy_primary_key_to_domain_object() {
            var primaryKey = randomUUID();
            var input = ballotEntity(primaryKey);

            var result = mapper.ballotEntityToBallot(input);

            assertThat(result).isInstanceOf(JpaBackedBallot.class).hasFieldOrPropertyWithValue("id", primaryKey);
        }
    }

    @Nested
    class VoteMapping {
        @Test
        void should_copy_primary_key_from_database_retrieved_entity() {
            var primaryKey = randomUUID();
            var input = new JpaBackedVote(primaryKey, new Ballot("", ""), new Option(0, ""));

            var result = mapper.voteToVoteEntity(input);

            assertThat(result.getId()).isEqualTo(primaryKey);
        }

        @Test
        void should_copy_primary_key_to_domain_object() {
            var primaryKey = randomUUID();
            var input = new VoteEntity();
            input.setId(primaryKey);
            input.setBallot(ballotEntity(UUID.randomUUID()));
            input.setOption(pollOptionEntity(UUID.randomUUID()));

            var result = mapper.voteEntityToVote(input);

            assertThat(result).isInstanceOf(JpaBackedVote.class).hasFieldOrPropertyWithValue("id", primaryKey);
        }
    }

    private BallotEntity ballotEntity(UUID primaryKey) {
        var result = new BallotEntity();
        result.setId(primaryKey);
        result.setClientIdentifier("");
        result.setTicketId("");
        return result;
    }

    private PollOptionEntity pollOptionEntity(UUID primaryKey) {
        var result = new PollOptionEntity();
        result.setId(primaryKey);
        result.setOptionValue(0);
        result.setDisplayValue("");
        return result;
    }
}

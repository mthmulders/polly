package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.database.AbstractJpaRepositoryTest;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JpaPollRepositoryIT extends AbstractJpaRepositoryTest<PollRepository, JpaPollRepository> {
    private final PollMapper pollMapper = MapStructHelper.getMapper(PollMapper.class);

    @BeforeEach
    void prepare() {
        prepare(em -> new JpaPollRepository(em, pollMapper));
    }

    @Test
    void lookup_using_slugs_should_succeed() {
        var original = preparePoll("What's up?", "test-poll-1");

        var result = repository.findBySlug("test-poll-1");

        assertThat(result).isPresent().hasValueSatisfying(poll -> {
            assertThat(poll.getSlug()).isEqualTo(original.getSlug());
            assertThat(poll.getQuestion()).isEqualTo(original.getQuestion());
        });
    }

    @Test
    void retrieving_a_poll_should_include_its_options() {
        var options = new Option[] {new Option(1, "I'm good"), new Option(2, "So-so")};
        preparePoll("What's up?", "test-poll-2", options);

        var result = repository.findBySlug("test-poll-2");

        assertThat(result).isPresent().hasValueSatisfying(poll -> {
            assertThat(poll.getOptions()).hasSize(2);
            assertThat(poll.getOptions()).containsOnly(options);
        });
    }

    @Test
    void retrieving_a_poll_should_include_the_ballots() {
        var clientIdentifier = UUID.randomUUID().toString();
        var options = new Option[] {new Option(1, "I'm good"), new Option(2, "So-so")};
        preparePoll("What's up?", "test-poll-3", options);

        var ballot = repository
                .findBySlug("test-poll-3")
                .map(poll -> {
                    var _ballot = poll.requestBallot(clientIdentifier);
                    runTransactional(() -> repository.store(poll));
                    return _ballot;
                })
                .orElseThrow();

        var result = repository.findBySlug("test-poll-3");

        assertThat(result).isPresent().hasValueSatisfying(poll -> {
            assertThat(poll.getBallots()).containsOnly(ballot);
        });
    }

    private PollEntity preparePoll(String question, String slug, Option... options) {
        var poll = new PollEntity();
        poll.setSlug(slug);
        poll.setQuestion(question);

        var pollOptions = Arrays.stream(options)
                .map(pollMapper::optionToPollOptionEntity)
                .peek(option -> option.setPoll(poll)) // Link the option back to the poll
                .collect(Collectors.toSet());
        poll.setOptions(pollOptions);

        return persist(poll);
    }
}

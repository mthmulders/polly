package it.mulders.polly.infra.votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.database.AbstractJpaRepositoryTest;
import it.mulders.polly.infra.polls.PollMapper;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JpaBallotRepositoryIT extends AbstractJpaRepositoryTest<BallotRepository, JpaBallotRepository> {
    private final BallotMapper ballotMapper = MapStructHelper.getMapper(BallotMapper.class);
    private final PollMapper pollMapper = MapStructHelper.getMapper(PollMapper.class);

    @BeforeEach
    void prepare() {
        prepare(em -> new JpaBallotRepository(em, ballotMapper));
    }

    private Poll prepareHelperEntities(String slug) {
        var poll = new Poll("How are you", slug, Set.of(new Option(1, "I'm good"), new Option(2, "So-so")));
        var pollEntity = pollMapper.pollToPollEntity(poll);
        pollEntity.getOptions().forEach(pollOptionEntity -> pollOptionEntity.setPoll(pollEntity));
        persist(pollEntity);
        return poll;
    }

    @Test
    void should_store_ballot_for_poll() {
        var clientIdentifier = UUID.randomUUID().toString();
        var poll = prepareHelperEntities("should-store-ballot-for-poll");
        var ticketId = ticketId();
        var ballot = new Ballot(poll, clientIdentifier, ticketId);
        runTransactional(() -> repository.store(ballot));

        assertThat(repository.findByTicketId(ticketId)).hasValue(ballot);
    }

    @Test
    void should_not_find_by_non_existing_ticketId() {
        var clientIdentifier = UUID.randomUUID().toString();
        var poll = prepareHelperEntities("should-not-find-by-non-existing-ticketId");
        var ticketId = ticketId();
        var ballot = new Ballot(poll, clientIdentifier, ticketId);
        runTransactional(() -> repository.store(ballot));

        assertThat(repository.findByTicketId(ticketId())).isEmpty();
    }

    @Test
    void should_find_by_poll_and_clientIdentifier() {
        var clientIdentifier = UUID.randomUUID().toString();
        var ticketId = ticketId();
        var poll = prepareHelperEntities("should-find-by-poll-and-clientIdentifier");
        var ballot = new Ballot(poll, clientIdentifier, ticketId);
        runTransactional(() -> repository.store(ballot));

        assertThat(repository.findByPollAndClientIdentifier(poll, clientIdentifier))
                .hasValue(ballot);
    }

    @Test
    void should_not_find_by_nonexistent_poll() {
        var clientIdentifier = UUID.randomUUID().toString();
        var ticketId = ticketId();
        var poll = prepareHelperEntities("should-not-find-by-nonexistent-poll-1");
        var ballot = new Ballot(poll, clientIdentifier, ticketId);
        runTransactional(() -> repository.store(ballot));

        var anotherPoll = prepareHelperEntities("should-not-find-by-nonexistent-poll-2");

        assertThat(repository.findByPollAndClientIdentifier(anotherPoll, clientIdentifier))
                .isEmpty();
    }

    @Test
    void should_not_find_by_nonexistent_clientIdentifier() {
        var ticketId = ticketId();
        var poll = prepareHelperEntities("should-not-find_by-nonexistent-clientIdentifier");
        var ballot = new Ballot(poll, UUID.randomUUID().toString(), ticketId);
        runTransactional(() -> repository.store(ballot));

        assertThat(repository.findByPollAndClientIdentifier(
                        poll, UUID.randomUUID().toString()))
                .isEmpty();
    }

    private String ticketId() {
        return new Object().toString().replace("java.lang.Object@", "");
    }
}

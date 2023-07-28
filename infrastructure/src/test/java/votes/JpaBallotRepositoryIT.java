package votes;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.BallotRepository;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.database.AbstractJpaRepositoryTest;
import it.mulders.polly.infra.polls.PollMapper;
import it.mulders.polly.infra.votes.BallotMapper;
import it.mulders.polly.infra.votes.JpaBallotRepository;
import jakarta.persistence.EntityManager;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class JpaBallotRepositoryIT extends AbstractJpaRepositoryTest<BallotRepository, JpaBallotRepository> {
    private final BallotMapper ballotMapper = MapStructHelper.getMapper(BallotMapper.class);
    private final PollMapper pollMapper = MapStructHelper.getMapper(PollMapper.class);

    @BeforeEach
    void prepare() {
        prepare(em -> new JpaBallotRepository(em, ballotMapper));
    }

    private Poll prepareHelperEntities(String slug) {
        var poll = new Poll("How are you", slug, Set.of(new Option("I'm good"), new Option("So-so")));
        var pollEntity = pollMapper.pollToPollEntity(poll);
        pollEntity.getOptions().forEach(pollOptionEntity -> pollOptionEntity.setPoll(pollEntity));
        persist(pollEntity);
        return poll;
    }

    @Test
    void should_store_ballot_for_poll() {
        var ticketId = "01234567";
        var poll = prepareHelperEntities("should-store-ballot-for-poll");
        var ballot = new Ballot(poll, ticketId);
        runTransactional(() -> repository.store(ballot));

        assertThat(repository.findByTicketId(ticketId)).hasValue(ballot);
    }
}

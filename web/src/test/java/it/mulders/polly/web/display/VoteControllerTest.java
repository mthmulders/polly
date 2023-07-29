package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.VotingServiceImpl;
import it.mulders.polly.web.test.InMemoryPollRepository;
import jakarta.mvc.Models;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.core.ModelsImpl;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VoteControllerTest implements WithAssertions {
    private final Models models = new ModelsImpl();
    private final Poll poll = new Poll("", "whatever", Collections.emptySet());
    private final InMemoryPollRepository pollRepository = new InMemoryPollRepository(Set.of(poll));
    private final DummmyVotingService votingService = new DummmyVotingService();

    @Test
    void without_matching_poll_instance_should_return_NotFound() {
        var controller = new VoteController(models, pollRepository, votingService);

        var response = controller.displayVotePage("non-existing");

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_return_OK() {
        var controller = new VoteController(models, pollRepository, votingService);

        var response = controller.displayVotePage(poll.getSlug());

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_populate_model() {
        var controller = new VoteController(models, pollRepository, votingService);

        controller.displayVotePage(poll.getSlug());

        assertThat(models.get("poll")).isEqualTo(poll);
        assertThat(models.get("ballot")).isNotNull();
    }

    @Test
    void with_matching_poll_instance_should_create_ballot() {
        var controller = new VoteController(models, pollRepository, votingService);

        controller.displayVotePage(poll.getSlug());

        assertThat(votingService.ballotsCreatedFor).contains(poll);
    }

    private static class DummmyVotingService extends VotingServiceImpl {
        public final Collection<Poll> ballotsCreatedFor = new HashSet<>();

        public DummmyVotingService() {
            super(null);
        }

        @Override
        public Ballot createBallotFor(Poll poll) {
            ballotsCreatedFor.add(poll);
            return new Ballot(poll, "01234567");
        }
    }
}

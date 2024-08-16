package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.web.display.dto.OptionWithPercentage;
import it.mulders.polly.web.test.InMemoryPollRepository;
import jakarta.mvc.Models;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.core.ModelsImpl;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ShowPollVotesFragmentControllerTest implements WithAssertions {
    private final Models models = new ModelsImpl();
    private final InMemoryPollRepository pollRepository = new InMemoryPollRepository(Set.of());
    private final VoteSummaryService voteSummaryService = new VoteSummaryService() {
        @Override
        public int calculateVoteCount(Poll poll) {
            return 1;
        }

        @Override
        public List<OptionWithPercentage> calculateVotePercentages(Poll poll) {
            return List.of();
        }
    };

    @Test
    void without_matching_poll_instance_should_return_NotFound() {
        var controller = new ShowPollVotesFragmentController(models, pollRepository, voteSummaryService);

        var response = controller.show("whatever", "true");

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_return_OK() {
        pollRepository.add(new Poll("", "whatever", Collections.emptySet()));
        var controller = new ShowPollVotesFragmentController(models, pollRepository, voteSummaryService);

        var response = controller.show("whatever", "true");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void without_hxrequest_header_should_return_BadRequest() {
        pollRepository.add(new Poll("", "whatever", Collections.emptySet()));
        var controller = new ShowPollVotesFragmentController(models, pollRepository, voteSummaryService);

        var response = controller.show("whatever", null);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_populate_model() {
        var poll = new Poll("", "whatever", Collections.emptySet());
        pollRepository.add(poll);
        var controller = new ShowPollVotesFragmentController(models, pollRepository, voteSummaryService);

        controller.show("whatever", "true");

        assertThat(models.get("poll")).isEqualTo(poll);
        assertThat(models.get("voteCount")).isNotNull();
        assertThat(models.get("votePercentages")).isNotNull();
    }
}

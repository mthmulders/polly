package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.web.krazo.ApplicationUrlHelper;
import it.mulders.polly.web.test.InMemoryPollRepository;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.core.ModelsImpl;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ShowPollControllerTest implements WithAssertions {
    private final String voteUrl = "http://localhost:9080/vote/my-poll/my-instance";
    private final ApplicationUrlHelper urlHelper = new ApplicationUrlHelper() {
        @Override
        public String voteUrlForPoll(Poll poll) {
            return voteUrl;
        }
    };

    @Test
    void without_matching_poll_instance_should_return_NotFound() {
        var models = new ModelsImpl();
        var repository = new InMemoryPollRepository(Set.of());
        var controller = new ShowPollController(models, repository, urlHelper);

        var response = controller.show("whatever");

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_return_OK() {
        var models = new ModelsImpl();
        var poll = new Poll("", "whatever", Collections.emptySet());
        var repository = new InMemoryPollRepository(Set.of(poll));
        var controller = new ShowPollController(models, repository, urlHelper);

        var response = controller.show("whatever");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_populate_model() {
        var models = new ModelsImpl();
        var poll = new Poll("", "whatever", Collections.emptySet());
        var repository = new InMemoryPollRepository(Set.of(poll));
        var controller = new ShowPollController(models, repository, urlHelper);

        controller.show("whatever");

        assertThat(models.get("poll")).isEqualTo(poll);
        assertThat(models.get("voteUrl")).isEqualTo(voteUrl);
    }
}

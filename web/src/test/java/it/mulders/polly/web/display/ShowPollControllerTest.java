package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollInstance;
import it.mulders.polly.web.test.InMemoryPollInstanceRepository;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.core.ModelsImpl;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ShowPollControllerTest implements WithAssertions {

    @Test
    void without_matching_poll_instance_should_return_NotFound() {
        var models = new ModelsImpl();
        var repository = new InMemoryPollInstanceRepository(Set.of());
        var controller = new ShowPollController(models, repository);

        var response = controller.show("whatever", "whatever");

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_return_OK() {
        var models = new ModelsImpl();
        var pollInstance = new PollInstance(new Poll("", "whatever"), "whatever");
        var repository = new InMemoryPollInstanceRepository(Set.of(pollInstance));
        var controller = new ShowPollController(models, repository);

        var response = controller.show("whatever", "whatever");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_populate_model() {
        var models = new ModelsImpl();
        var pollInstance = new PollInstance(new Poll("", "whatever"), "whatever");
        var repository = new InMemoryPollInstanceRepository(Set.of(pollInstance));
        var controller = new ShowPollController(models, repository);

        var response = controller.show("whatever", "whatever");

        assertThat(models.get("pollInstance")).isEqualTo(pollInstance);
    }
}
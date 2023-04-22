package it.mulders.polly.web.display;

import it.mulders.polly.domain.PollInstanceRepository;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Controller
public class ShowPollController {
    private final Models models;
    private final PollInstanceRepository pollInstanceRepository;

    @Inject
    public ShowPollController(final Models models, final PollInstanceRepository pollInstanceRepository) {
        this.models = models;
        this.pollInstanceRepository = pollInstanceRepository;
    }

    @GET
    @Path("/{poll-slug}/{instance-slug}/show")
    @Produces("text/html; charset=UTF-8")
    public Response show(@PathParam("poll-slug") String pollSlug, @PathParam("instance-slug") String instanceSlug) {
        return pollInstanceRepository.findBySlugs(pollSlug, instanceSlug)
                .map(pollInstance -> models.put("pollInstance", pollInstance))
                .map(models -> Response.ok("poll/show.jsp").build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}

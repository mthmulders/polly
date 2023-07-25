package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.PollInstance;
import it.mulders.polly.domain.polls.PollInstanceRepository;
import it.mulders.polly.web.krazo.ApplicationUrlHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/vote")
@Controller
@RequestScoped
public class VoteController {
    @Inject
    private Models models;

    @Inject
    private PollInstanceRepository pollInstanceRepository;

    public VoteController() {}

    VoteController(Models models, PollInstanceRepository pollInstanceRepository) {
        this.models = models;
        this.pollInstanceRepository = pollInstanceRepository;
    }

    @GET
    @Path("/{poll-slug}/{instance-slug}")
    @Produces("text/html; charset=UTF-8")
    public Response displayVotePage(@PathParam("poll-slug") String pollSlug, @PathParam("instance-slug") String instanceSlug) {
        return pollInstanceRepository.findByPollSlugAndInstanceSlug(pollSlug, instanceSlug)
                .map(this::populateModelAndPrepareResponse)
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private Response populateModelAndPrepareResponse(final PollInstance pollInstance) {
        models.put("pollInstance", pollInstance);
        return Response.ok("polls/vote.jsp").build();
    }
}

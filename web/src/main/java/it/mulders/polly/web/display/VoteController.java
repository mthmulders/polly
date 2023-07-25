package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
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
    private PollRepository pollRepository;

    public VoteController() {}

    VoteController(Models models, PollRepository pollRepository) {
        this.models = models;
        this.pollRepository = pollRepository;
    }

    @GET
    @Path("/{slug}")
    @Produces("text/html; charset=UTF-8")
    public Response displayVotePage(@PathParam("slug") String slug) {
        return pollRepository.findBySlug(slug)
                .map(this::populateModelAndPrepareResponse)
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private Response populateModelAndPrepareResponse(final Poll poll) {
        models.put("poll", poll);
        return Response.ok("polls/vote.jsp").build();
    }
}

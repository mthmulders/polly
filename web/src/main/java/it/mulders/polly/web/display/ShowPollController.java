package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
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

@Path("/show")
@Controller
@RequestScoped
public class ShowPollController {
    @Inject
    private Models models;

    @Inject
    private PollRepository pollRepository;

    @Inject
    private ApplicationUrlHelper urlHelper;

    public ShowPollController() {}

    ShowPollController(Models models, PollRepository pollRepository, ApplicationUrlHelper urlHelper) {
        this.models = models;
        this.pollRepository = pollRepository;
        this.urlHelper = urlHelper;
    }

    @GET
    @Path("/{slug}")
    @Produces("text/html; charset=UTF-8")
    public Response show(@PathParam("slug") String slug) {
        return pollRepository
                .findBySlug(slug)
                .map(this::populateModelAndPrepareResponse)
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private Response populateModelAndPrepareResponse(final Poll poll) {
        models.put("poll", poll);
        models.put("voteUrl", urlHelper.voteUrlForPoll(poll));
        return Response.ok("polls/show.jsp").build();
    }
}

package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.logging.Logger;

@Controller
@Path("/show")
@RequestScoped
public class ShowPollVotesFragmentController {
    private static final Logger log = Logger.getLogger(ShowPollVotesFragmentController.class.getName());

    @Inject
    private Models models;

    @Inject
    private PollRepository pollRepository;

    @Inject
    private VoteSummaryService voteSummaryService;

    public ShowPollVotesFragmentController() {}

    ShowPollVotesFragmentController(
            Models models, PollRepository pollRepository, VoteSummaryService voteSummaryService) {
        this.models = models;
        this.pollRepository = pollRepository;
        this.voteSummaryService = voteSummaryService;
    }

    @GET
    @Path("/{slug}/votes")
    @Produces("text/html; charset=UTF-8")
    public Response show(@PathParam("slug") String slug, @HeaderParam("HX-Request") String hxRequest) {
        if (!"true".equals(hxRequest)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return pollRepository
                .findBySlug(slug)
                .map(this::populateModelAndPrepareResponse)
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private Response populateModelAndPrepareResponse(final Poll poll) {
        models.put("votePercentages", voteSummaryService.calculateVotePercentages(poll));
        models.put("voteCount", voteSummaryService.calculateVoteCount(poll));
        models.put("poll", poll);

        return Response.ok("fragments/polls/_votes.jsp").build();
    }
}

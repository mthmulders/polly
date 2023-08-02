package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.VotingService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.MvcContext;
import jakarta.transaction.Transactional;
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

    @Inject
    private VotingService votingService;

    public VoteController() {}

    VoteController(Models models, PollRepository pollRepository, VotingService votingService) {
        this.models = models;
        this.pollRepository = pollRepository;
        this.votingService = votingService;
    }

    @GET
    @Path("/{slug}")
    @Produces("text/html; charset=UTF-8")
    @Transactional
    public Response displayVotePage(@PathParam("slug") String slug) {
        return pollRepository.findBySlug(slug)
                .map(this::prepareBallot)
                .map(this::populateModelAndPrepareResponse)
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private Ballot prepareBallot(final Poll poll) {
        // TODO Must add some client identification (like the JSESSIONID)
        return votingService.requestBallotFor(poll, "");
    }

    private Response populateModelAndPrepareResponse(final Ballot ballot) {
        models.put("ballot", ballot);
        models.put("poll", ballot.poll());
        return Response.ok("polls/vote.jsp").build();
    }
}

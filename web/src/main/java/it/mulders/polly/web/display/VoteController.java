package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;
import it.mulders.polly.domain.votes.VotingService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
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

    @Context
    @Inject
    private HttpServletRequest request;

    public VoteController() {}

    VoteController(Models models, PollRepository pollRepository, HttpServletRequest request, VotingService votingService) {
        this.models = models;
        this.pollRepository = pollRepository;
        this.request = request;
        this.votingService = votingService;
    }

    @GET
    @Path("/{slug}")
    @Produces("text/html; charset=UTF-8")
    @Transactional
    public Response displayVotePage(@PathParam("slug") String slug) {
        return pollRepository.findBySlug(slug)
                .map(poll -> prepareResponseAfterRequestingBallot(prepareBallot(poll), poll))
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{slug}")
    @Produces("text/html; charset=UTF-8")
    @Transactional
    public Response processVote(@PathParam("slug") String slug,
                                @FormParam("ballot.ticketId") String ticketId,
                                @FormParam("vote.selectedOption") Integer selectedOption) {
        return pollRepository.findBySlug(slug)
                .map(poll -> votingService.castVote(poll, ticketId, selectedOption)
                        .map(vote -> prepareResponseAfterSuccessfulVote(poll, vote))
                        .getOrElseConvertCause(error -> prepareResponseAfterUnsuccessfulVote(poll, error)))
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private Ballot prepareBallot(final Poll poll) {
        var sessionId = request.getSession(true).getId();
        return votingService.requestBallotFor(poll, sessionId);
    }

    private Response prepareResponseAfterRequestingBallot(final Ballot ballot, final Poll poll) {
        models.put("ballot", ballot);
        models.put("poll", poll);
        return Response.ok("polls/vote.jsp").build();
    }

    private Response prepareResponseAfterSuccessfulVote(final Poll poll, final Vote vote) {
        models.put("poll", poll);
        models.put("vote", vote);
        return Response.ok("polls/thanks.jsp").build();
    }

    private Response prepareResponseAfterUnsuccessfulVote(final Poll poll, final Throwable error) {
        models.put("error", error);
        models.put("poll", poll);
        return Response.ok("polls/vote.jsp").build();
    }
}

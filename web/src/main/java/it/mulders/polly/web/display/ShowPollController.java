package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.PollInstanceRepository;
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
    private PollInstanceRepository pollInstanceRepository;

    @GET
    @Path("/{poll-slug}/{instance-slug}")
    @Produces("text/html; charset=UTF-8")
    public Response show(@PathParam("poll-slug") String pollSlug, @PathParam("instance-slug") String instanceSlug) {
        return pollInstanceRepository.findBySlugs(pollSlug, instanceSlug)
                .map(pollInstance -> models.put("pollInstance", pollInstance))
                .map(x -> Response.ok("polls/show.jsp").build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}

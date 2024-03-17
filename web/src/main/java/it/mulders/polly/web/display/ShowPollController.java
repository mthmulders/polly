package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.web.display.qr.QRCodeGenerator;
import it.mulders.polly.web.display.qr.QRGenerationException;
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
    private QRCodeGenerator qrCodeGenerator;

    @Inject
    private ApplicationUrlHelper urlHelper;

    public ShowPollController() {}

    ShowPollController(
            Models models,
            PollRepository pollRepository,
            QRCodeGenerator qrCodeGenerator,
            ApplicationUrlHelper urlHelper) {
        this.models = models;
        this.pollRepository = pollRepository;
        this.qrCodeGenerator = qrCodeGenerator;
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
        models.put("qrCodeBody", prepareQRCode(poll));
        models.put("qrCodeViewBox", QRCodeGenerator.QR_CODE_DIMENSION_VIEWBOX);
        models.put("poll", poll);
        models.put("voteCount", poll.getVotes().size());
        models.put("votePercentages", poll.calculateVotePercentages());

        return Response.ok("polls/show.jsp").build();
    }

    private String prepareQRCode(final Poll poll) {
        try {
            var voteUrl = urlHelper.voteUrlForPollSlug(poll.getSlug());
            var path = qrCodeGenerator.generateQRCodeSvgPath(voteUrl);
            return "<path d=\"%s\" />".formatted(path);
        } catch (QRGenerationException e) {
            return "<text x=\"0\" y=\"0\">Error generating QR code.</text>";
        }
    }
}

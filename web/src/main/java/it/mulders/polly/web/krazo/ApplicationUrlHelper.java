package it.mulders.polly.web.krazo;

import it.mulders.polly.domain.polls.Poll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.MvcContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ApplicationUrlHelper {
    private static final Logger logger = Logger.getLogger(ApplicationUrlHelper.class.getName());

    @Inject
    private MvcContext mvcContext;

    @Inject
    @Context
    private HttpServletRequest request;

    public ApplicationUrlHelper() {
    }

    ApplicationUrlHelper(MvcContext mvcContext, HttpServletRequest request) {
        this.mvcContext = mvcContext;
        this.request = request;
    }

    public String voteUrlForPoll(Poll poll) {
        try {
            var requestUrl = new URL(request.getRequestURL().toString());

            var protocol = requestUrl.getProtocol();
            var port = requestUrl.getPort();

            var isNonDefaultPort = port != -1;
            var host = requestUrl.getHost() + (isNonDefaultPort ? ":" + port : "");

            var params = Map.of(
                    "slug", (Object) poll.slug()
            );
            var voteUrl = mvcContext.uri("VoteController#displayVotePage", params);

            return "%s://%s%s%s".formatted(
                    protocol,
                    host,
                    mvcContext.getBasePath(),
                    voteUrl
            );
        } catch (MalformedURLException murle) {
            logger.log(Level.WARNING, "Could not determine Vote URL for poll instance", murle);
        }
        return "foo";
    }
}

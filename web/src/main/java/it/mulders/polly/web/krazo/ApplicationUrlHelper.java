package it.mulders.polly.web.krazo;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.shared.ConfigurationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mvc.MvcContext;
import java.util.Map;

@ApplicationScoped
public class ApplicationUrlHelper {
    @Inject
    private ConfigurationService configurationService;

    @Inject
    private MvcContext mvcContext;

    public ApplicationUrlHelper() {}

    ApplicationUrlHelper(ConfigurationService configurationService, MvcContext mvcContext) {
        this.configurationService = configurationService;
        this.mvcContext = mvcContext;
    }

    public String voteUrlForPoll(Poll poll) {
        var applicationUrl = configurationService.applicationUrl();

        var params = Map.of("slug", (Object) poll.slug());
        var voteUrl = mvcContext.uri("VoteController#displayVotePage", params);

        return "%s%s".formatted(applicationUrl.toString(), voteUrl);
    }
}

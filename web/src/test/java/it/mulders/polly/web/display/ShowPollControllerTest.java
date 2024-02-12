package it.mulders.polly.web.display;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.web.display.qr.QRCodeGenerator;
import it.mulders.polly.web.display.qr.QRGenerationException;
import it.mulders.polly.web.krazo.ApplicationUrlHelper;
import it.mulders.polly.web.test.InMemoryPollRepository;
import jakarta.mvc.Models;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.core.ModelsImpl;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ShowPollControllerTest implements WithAssertions {
    private final String voteUrl = "http://localhost:9080/vote/my-poll/my-instance";
    private final Models models = new ModelsImpl();
    private final InMemoryPollRepository pollRepository = new InMemoryPollRepository(Set.of());
    private final QRCodeGenerator qrCodeGenerator = new QRCodeGenerator() {
        @Override
        public String generateQRCodeSvgPath(String text) {
            return "";
        }
    };
    private final ApplicationUrlHelper urlHelper = new ApplicationUrlHelper() {
        @Override
        public String voteUrlForPollSlug(String pollSlug) {
            return voteUrl;
        }
    };

    @Test
    void without_matching_poll_instance_should_return_NotFound() {
        var controller = new ShowPollController(models, pollRepository, qrCodeGenerator, urlHelper);

        var response = controller.show("whatever");

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_return_OK() {
        pollRepository.add(new Poll("", "whatever", Collections.emptySet()));
        var controller = new ShowPollController(models, pollRepository, qrCodeGenerator, urlHelper);

        var response = controller.show("whatever");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void with_matching_poll_instance_should_populate_model() {
        var poll = new Poll("", "whatever", Collections.emptySet());
        pollRepository.add(poll);
        var controller = new ShowPollController(models, pollRepository, qrCodeGenerator, urlHelper);

        controller.show("whatever");

        assertThat(models.get("poll")).isEqualTo(poll);
        assertThat(models.get("qrCodeBody")).isNotNull();
        assertThat(models.get("qrCodeViewBox")).isNotNull();
    }

    @Test
    void error_generating_qr_should_show_error() {
        var poll = new Poll("", "whatever", Collections.emptySet());
        pollRepository.add(poll);
        var qrCodeGenerator = new QRCodeGenerator() {
            @Override
            public String generateQRCodeSvgPath(String text) throws QRGenerationException {
                throw new QRGenerationException(new NullPointerException());
            }
        };
        var controller = new ShowPollController(models, pollRepository, qrCodeGenerator, urlHelper);

        controller.show("whatever");

        assertThat(models.get("qrCodeBody").toString()).containsSequence("Error generating QR code");
    }
}

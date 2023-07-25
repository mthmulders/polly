package it.mulders.polly.web.krazo;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.shared.ConfigurationService;
import jakarta.mvc.MvcContext;
import jakarta.servlet.ServletContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.MvcContextImpl;
import org.eclipse.krazo.uri.ApplicationUris;
import org.eclipse.krazo.uri.UriTemplate;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.mock.web.MockServletContext;

class ApplicationUrlHelperTest implements WithAssertions {
    private final MvcContext mvcContext = new MvcContextImpl();

    private final Poll poll = new Poll("That's the question", "my-question");

    @ArgumentsSource(ApplicationUrlTestArgumentsProvider.class)
    @ParameterizedTest(name = "Should generate correct URL {0}")
    void should_generate_correct_url(final String displayName, final String applicationUrl, final String expectedResult)
            throws NoSuchMethodException, ClassNotFoundException, MalformedURLException, URISyntaxException {
        var servletContext = new MockServletContext();
        servletContext.setContextPath(determineContextPath(applicationUrl));
        setServletContext(servletContext);

        var controllerMethod = Class.forName("it.mulders.polly.web.display.VoteController")
                .getDeclaredMethod("displayVotePage", String.class);
        setApplicationUris(prepareApplicationUris("/vote/{slug}", controllerMethod));

        var configurationService = new ConfigurationService() {
            @Override
            public URL applicationUrl() {
                try {
                    return new URL(applicationUrl);
                } catch (MalformedURLException e) {
                    fail("Failed to prepare " + getClass(), e);
                    return null;
                }
            }
        };

        var result = new ApplicationUrlHelper(configurationService, mvcContext).voteUrlForPoll(poll);

        assertThat(result).isEqualTo(expectedResult);
    }

    private String determineContextPath(final String applicationUrl) throws MalformedURLException {
        var url = new URL(applicationUrl);
        return url.getPath();
    }

    private static class ApplicationUrlTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext context) {
            return Stream.of(
                    Arguments.of("on HTTP with standard port", "http://localhost", "http://localhost/vote/my-question"),
                    Arguments.of(
                            "on HTTP with context and standard port",
                            "http://localhost/polly",
                            "http://localhost/polly/vote/my-question"),
                    Arguments.of(
                            "on HTTP with non-standard port",
                            "http://localhost:9080",
                            "http://localhost:9080/vote/my-question"),
                    Arguments.of(
                            "on HTTP with context and non-standard port",
                            "http://localhost:9080/polly",
                            "http://localhost:9080/polly/vote/my-question"),
                    Arguments.of(
                            "on HTTPS with standard port", "https://localhost", "https://localhost/vote/my-question"),
                    Arguments.of(
                            "on HTTPS with non-standard port",
                            "https://localhost:9443",
                            "https://localhost:9443/vote/my-question"),
                    Arguments.of(
                            "on HTTPS with non-standard port",
                            "https://localhost:9443",
                            "https://localhost:9443/vote/my-question"),
                    Arguments.of(
                            "on HTTPS with context and non-standard port",
                            "https://localhost:9443/polly",
                            "https://localhost:9443/polly/vote/my-question"));
        }
    }

    private void setApplicationUris(ApplicationUris applicationUris) {
        try {
            var field = mvcContext.getClass().getDeclaredField("applicationUris");
            field.setAccessible(true);
            field.set(mvcContext, applicationUris);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to prepare " + getClass(), e);
        }
    }

    private void setServletContext(ServletContext servletContext) {
        try {
            var field = mvcContext.getClass().getDeclaredField("servletContext");
            field.setAccessible(true);
            field.set(mvcContext, servletContext);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to prepare " + getClass(), e);
        }
    }

    private ApplicationUris prepareApplicationUris(String templateStr, Method method) {
        var applicationUris = new ApplicationUris();
        try {
            // Invoke UriTemplate.fromTemplate(template)
            var fromTemplateMethod = UriTemplate.class.getDeclaredMethod("fromTemplate", String.class);
            fromTemplateMethod.setAccessible(true);
            var builder = fromTemplateMethod.invoke(null, templateStr);

            // Invoke UriTemplate.Builder.build()
            // spotless:off
            var buildMethod = Class.forName("org.eclipse.krazo.uri.UriTemplate$Builder").getDeclaredMethod("build");
            // spotless:on
            buildMethod.setAccessible(true);
            var template = (UriTemplate) buildMethod.invoke(builder);

            // Invoke ApplicationUris.register(template, method)
            var registerMethod = ApplicationUris.class.getDeclaredMethod("register", UriTemplate.class, Method.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(applicationUris, template, method);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException e) {
            fail("Failed to prepare " + getClass(), e);
        }
        return applicationUris;
    }
}

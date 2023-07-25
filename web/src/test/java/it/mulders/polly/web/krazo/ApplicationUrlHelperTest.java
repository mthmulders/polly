package it.mulders.polly.web.krazo;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollInstance;
import jakarta.mvc.MvcContext;
import jakarta.servlet.ServletContext;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.MvcContextImpl;
import org.eclipse.krazo.uri.ApplicationUris;
import org.eclipse.krazo.uri.UriTemplate;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

class ApplicationUrlHelperTest implements WithAssertions {
    private final MvcContext mvcContext = new MvcContextImpl();
//    private final MockHttpServletRequest request = new MockHttpServletRequest();
//    private final ApplicationUrlHelper helper = new ApplicationUrlHelper(mvcContext, request);

    private final PollInstance instance = new PollInstance(
            new Poll("That's the question", "my-question"),
            "my-instance"
    );

    @ArgumentsSource(ApplicationUrlTestArgumentsProvider.class)
    @ParameterizedTest(name = "Should generate correct URL {0}")
    void should_generate_correct_url(final String displayName, final String requestUrl, final String expectedResult) throws NoSuchMethodException, ClassNotFoundException, MalformedURLException, URISyntaxException {
        var servletContext = new MockServletContext();
        servletContext.setContextPath(determineContextPath(requestUrl));
        setServletContext(servletContext);

        var requestUri = new URL(requestUrl).toURI();

        var request = new MockHttpServletRequest(servletContext);
        request.setScheme(requestUri.getScheme());
        request.setServerPort(requestUri.getPort());
        request.setRequestURI(requestUri.getPath());
        assumeTrue(request.getRequestURL().toString().equals(requestUrl));

        var controllerMethod = Class.forName("it.mulders.polly.web.display.VoteController").getDeclaredMethod("displayVotePage", String.class, String.class);
        setApplicationUris(prepareApplicationUris("/vote/{poll-slug}/{instance-slug}", controllerMethod));

        var result = new ApplicationUrlHelper(mvcContext, request).voteUrlForPollInstance(instance);

        assertThat(result).isEqualTo(expectedResult);
    }

    private String determineContextPath(final String requestUrl) throws MalformedURLException {
        var appRoot = requestUrl.replace("/show/my-question/my-instance", "");
        var url = new URL(appRoot);
        return url.getPath();
    }

    private static class ApplicationUrlTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext context) {
            return Stream.of(
                    Arguments.of("on HTTP with standard port", "http://localhost/show/my-question/my-instance", "http://localhost/vote/my-question/my-instance"),
                    Arguments.of("on HTTP with context and standard port", "http://localhost/polly/show/my-question/my-instance", "http://localhost/polly/vote/my-question/my-instance"),

                    Arguments.of("on HTTP with non-standard port", "http://localhost:9080/show/my-question/my-instance", "http://localhost:9080/vote/my-question/my-instance"),
                    Arguments.of("on HTTP with context and non-standard port", "http://localhost:9080/polly/show/my-question/my-instance", "http://localhost:9080/polly/vote/my-question/my-instance"),

                    Arguments.of("on HTTPS with standard port", "https://localhost/show/my-question/my-instance", "https://localhost/vote/my-question/my-instance"),
                    Arguments.of("on HTTPS with non-standard port", "https://localhost:9443/show/my-question/my-instance", "https://localhost:9443/vote/my-question/my-instance"),

                    Arguments.of("on HTTPS with non-standard port", "https://localhost:9443/show/my-question/my-instance", "https://localhost:9443/vote/my-question/my-instance"),
                    Arguments.of("on HTTPS with context and non-standard port", "https://localhost:9443/polly/show/my-question/my-instance", "https://localhost:9443/polly/vote/my-question/my-instance")
            );
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

            // Invoke UriTemplate.Builder.build();
            var buildMethod = Class.forName("org.eclipse.krazo.uri.UriTemplate$Builder").getDeclaredMethod("build");
            buildMethod.setAccessible(true);
            var template = (UriTemplate) buildMethod.invoke(builder);

            // Invoke ApplicationUris.register(template, method);
            var registerMethod = ApplicationUris.class.getDeclaredMethod("register", UriTemplate.class, Method.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(applicationUris, template, method);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to prepare " + getClass(), e);
        }
        return applicationUris;
    }
}
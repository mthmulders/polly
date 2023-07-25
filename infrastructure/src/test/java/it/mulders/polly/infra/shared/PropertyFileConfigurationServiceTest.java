package it.mulders.polly.infra.shared;

import java.net.MalformedURLException;
import java.net.URL;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PropertyFileConfigurationServiceTest implements WithAssertions {
    // A bit hacky way to be able to load a file that is actually on the classpath.
    private final String testClasspath =
            getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

    private final PropertyFileConfigurationService service = new PropertyFileConfigurationService();

    @Test
    void when_config_path_not_existing_should_fail() {
        assertThatThrownBy(() -> service.loadProperties("non-existing.properties"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-existing.properties could not be read");
    }

    @Test
    void when_config_path_not_specified_should_fail() {
        assertThatThrownBy(() -> service.loadProperties(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no configuration file given");
    }

    @Test
    void should_read_url_from_valid_config() throws MalformedURLException {
        service.loadProperties(testClasspath + "/valid_configuration.properties");

        assertThat(service.applicationUrl()).isEqualTo(new URL("http://localhost:9080/"));
    }

    @Test
    void should_fail_on_invalid_config() throws MalformedURLException {
        service.loadProperties(testClasspath + "/invalid_configuration.properties");

        assertThatThrownBy(() -> service.applicationUrl())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("foo");
    }
}

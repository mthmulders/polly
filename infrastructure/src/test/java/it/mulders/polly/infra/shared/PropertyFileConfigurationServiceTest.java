package it.mulders.polly.infra.shared;

import java.net.MalformedURLException;
import java.net.URL;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PropertyFileConfigurationServiceTest implements WithAssertions {
    // A bit hacky way to be able to load a file that is actually on the classpath.
    private final String testClasspath =
            getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

    private final PropertyFileConfigurationService service = new PropertyFileConfigurationService();

    @Test
    void should_read_default_paths() {
        service.loadConfigurationProperties();

        assertThat(service.applicationUrl()).isNotNull();
    }

    @DisplayName("Configuration properties")
    @Nested
    class ConfigurationProperties {

        @Test
        void when_config_path_not_existing_should_fail() {
            assertThatThrownBy(() -> service.loadConfigurationProperties("non-existing.properties"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not read configuration from non-existing.properties");
        }

        @Test
        void when_config_path_not_specified_should_fail() {
            assertThatThrownBy(() -> service.loadConfigurationProperties(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("no configuration file given");
        }

        @Test
        void should_read_url_from_valid_config() throws MalformedURLException {
            service.loadConfigurationProperties(testClasspath + "/valid_configuration.properties");

            assertThat(service.applicationUrl()).isEqualTo(new URL("http://localhost:9080/"));
        }

        @Test
        void should_fail_on_invalid_config() throws MalformedURLException {
            service.loadConfigurationProperties(testClasspath + "/invalid_configuration.properties");

            assertThatThrownBy(() -> service.applicationUrl())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("foo");
        }
    }
}

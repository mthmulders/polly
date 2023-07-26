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

    @DisplayName("Metadata properties")
    @Nested
    class MetadataProperties {
        @Test
        void when_metadata_path_not_existing_should_fail() {
            assertThatThrownBy(() -> service.loadMetadataProperties("/non-existing.properties"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("non-existing.properties does not exist");
        }

        @Test
        void when_metadata_path_not_specified_should_fail() {
            assertThatThrownBy(() -> service.loadMetadataProperties(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("No metadata resource supplied");
        }

        @Test
        void should_read_application_version_from_valid_metadata() throws MalformedURLException {
            service.loadMetadataProperties("/valid_metadata.properties");

            assertThat(service.applicationVersion()).isEqualTo("42");
            assertThat(service.gitVersion()).isEqualTo("0123456");
        }
    }
}

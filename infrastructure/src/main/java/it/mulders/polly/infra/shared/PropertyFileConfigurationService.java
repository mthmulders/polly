package it.mulders.polly.infra.shared;

import it.mulders.polly.domain.shared.ConfigurationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link ConfigurationService} that reads its values from a property file.
 * The location of the property file is configurable using the environment variable <pre>POLLY_CONFIGURATION_FILE</pre>.
 */
@ApplicationScoped
public class PropertyFileConfigurationService implements ConfigurationService {
    private static final Logger logger = Logger.getLogger(PropertyFileConfigurationService.class.getName());
    private static final String CONFIG_PATH_VARIABLE = "POLLY_CONFIGURATION_FILE";

    private final Properties properties = new Properties();

    @PostConstruct
    public void loadConfigurationProperties() {
        loadConfigurationProperties(System.getenv(CONFIG_PATH_VARIABLE));
        loadMetadataProperties("/git.properties");
        loadMetadataProperties("/application.properties");
    }

    void loadMetadataProperties(String classpathResourceName) {
        if (classpathResourceName == null) {
            var msg = "No metadata resource supplied";
            logger.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        try (final InputStream input = getClass().getResourceAsStream(classpathResourceName)) {
            if (input == null) {
                var msg = "Metadata resource %s does not exist".formatted(classpathResourceName);
                logger.warning(msg);
                throw new IllegalArgumentException(msg);
            }

            var sizeBefore = properties.size();
            properties.load(input);
            var sizeAfter = properties.size();
            logger.info(() -> "%d metadata value(s) loaded".formatted(sizeAfter - sizeBefore));
        } catch (IOException e) {
            var msg = "Could not read metadata from %s".formatted(classpathResourceName);
            logger.log(Level.SEVERE, msg, e);
            throw new IllegalArgumentException(msg);
        }
    }

    void loadConfigurationProperties(final String configFileLocation) {
        if (configFileLocation == null) {
            logger.severe(() ->
                    "Could not read configuration. Specify a path to the configuration file with the %s environment variable"
                            .formatted(CONFIG_PATH_VARIABLE));
            throw new IllegalArgumentException("Could not read configuration, no configuration file given");
        }

        logger.info(() -> "Loading configuration file %s".formatted(configFileLocation));
        try (var input = Files.newInputStream(Paths.get(configFileLocation))) {
            properties.load(input);
            logger.info(
                    () -> "%d configuration value(s) loaded from %s".formatted(properties.size(), configFileLocation));
        } catch (IOException e) {
            var msg = "Could not read configuration from %s".formatted(configFileLocation);
            logger.log(Level.SEVERE, msg, e);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public URL applicationUrl() {
        var key = "application.url";
        var applicationUrl = properties.getProperty(key);
        try {
            return new URL(applicationUrl);
        } catch (MalformedURLException e) {
            var msg = "Value '%s' for configuration setting '%s' is not a valid URL".formatted(applicationUrl, key);
            logger.log(Level.SEVERE, e, () -> msg);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public String applicationVersion() {
        return properties.getProperty("polly.version");
    }

    @Override
    public String gitVersion() {
        return properties.getProperty("git.commit.id.abbrev");
    }
}

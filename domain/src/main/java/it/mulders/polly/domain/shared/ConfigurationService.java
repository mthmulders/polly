package it.mulders.polly.domain.shared;

import java.net.URL;

/**
 * Exposes application-wide configuration values.
 */
public interface ConfigurationService {
    /**
     * The URL where the application is published.
     * @return A URL that - when accessed - results in the root of this application.
     */
    URL applicationUrl();

    /**
     * The human-readable version number for the application.
     * @return The Maven project version.
     */
    String applicationVersion();

    /**
     * The technical version number for the application.
     * @return The abbreviated Git commit identifier.
     */
    String gitVersion();
}

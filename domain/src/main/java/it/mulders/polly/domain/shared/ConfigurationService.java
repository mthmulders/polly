package it.mulders.polly.domain.shared;

import java.net.URL;

/**
 * Exposes application-wide configuration values.
 */
public interface ConfigurationService {
    /**
     * The URL where the application is published.
     * @return An URL that - when accessed - results in the root of this application.
     */
    URL applicationUrl();
}

package it.mulders.polly.web;

import it.mulders.polly.domain.shared.ConfigurationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Properties;

@ApplicationScoped
@Named("systemInfo")
public class SystemInfo {
    private final Properties systemProperties = System.getProperties();

    @Inject
    private ConfigurationService configurationService;

    public String getJavaVersion() {
        return (String) systemProperties.get("java.specification.version");
    }

    public String getJavaRuntime() {
        return "%s %s".formatted(
                systemProperties.get("java.vendor"),
                systemProperties.get("java.runtime.version")
        );
    }

    public String getApplicationVersion() {
        return configurationService.applicationVersion();
    }

    public String getGitVersion() {
        return configurationService.gitVersion();
    }
}

package it.mulders.polly.web;

import it.mulders.polly.domain.shared.ConfigurationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Properties;

@ApplicationScoped
@Named("systemInfo")
public class SystemInfo {
    private final Properties systemProperties;
    private final ConfigurationService configurationService;

    @Inject
    public SystemInfo(ConfigurationService configurationService) {
        this(configurationService, System.getProperties());
    }

    SystemInfo(ConfigurationService configurationService, Properties systemProperties) {
        this.configurationService = configurationService;
        this.systemProperties = systemProperties;
    }

    public String getJavaVersion() {
        return (String) systemProperties.get("java.specification.version");
    }

    public String getJavaRuntime() {
        return "%s %s".formatted(systemProperties.get("java.vm.vendor"), systemProperties.get("java.runtime.version"));
    }
}

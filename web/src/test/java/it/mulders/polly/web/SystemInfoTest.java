package it.mulders.polly.web;

import it.mulders.polly.domain.shared.ConfigurationService;
import java.net.URL;
import java.util.Properties;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SystemInfoTest implements WithAssertions {
    private final ConfigurationService configurationService = new DummyConfigurationService();
    private final Properties systemProperties = new Properties();
    private final SystemInfo systemInfo = new SystemInfo(configurationService, systemProperties);

    @Test
    void getJavaVersion_should_return_value_of_corresponding_system_property() {
        systemProperties.put("java.specification.version", "17");
        assertThat(systemInfo.getJavaVersion()).isEqualTo("17");
    }

    @Test
    void getJavaRuntime_should_return_value_of_corresponding_system_properties() {
        systemProperties.put("java.vm.vendor", "Eclipse OpenJ9");
        systemProperties.put("java.runtime.version", "17.0.7+7");
        assertThat(systemInfo.getJavaRuntime()).isEqualTo("Eclipse OpenJ9 17.0.7+7");
    }

    private static class DummyConfigurationService implements ConfigurationService {

        @Override
        public URL applicationUrl() {
            return null;
        }
    }
}

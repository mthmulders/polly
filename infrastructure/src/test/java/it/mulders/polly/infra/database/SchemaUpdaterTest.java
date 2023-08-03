package it.mulders.polly.infra.database;

import java.util.Collections;
import java.util.Map;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SchemaUpdaterTest implements WithAssertions {
    private final SchemaUpdater updater = new SchemaUpdater();

    @Test
    void should_use_default_location_for_migrations() {
        var locations = updater.determineFlywayLocations(Collections.emptyMap());

        assertThat(locations).containsExactly("classpath:db/migration", "classpath:db/migration-postgresql");
    }

    @Test
    void should_use_specified_location_for_migrations() {
        var environmentVariables = Map.of("FLYWAY_LOCATIONS", "classpath:a,classpath:b");
        var locations = updater.determineFlywayLocations(environmentVariables);

        assertThat(locations).containsExactly("classpath:a", "classpath:b");
    }
}

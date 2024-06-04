package it.mulders.polly.infra.database;

import java.util.Collections;
import java.util.Map;
import javax.sql.DataSource;
import org.assertj.core.api.WithAssertions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SchemaUpdaterTest implements WithAssertions {
    private final FluentConfiguration configuration = new FlywayConfigurationMock();
    private final FlywayMock flyway = new FlywayMock(configuration);
    private final SchemaUpdater updater = new SchemaUpdater() {
        @Override
        protected FluentConfiguration initFlyway() {
            return configuration;
        }
    };

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

    @Test
    void should_produce_Flyway_configuration() {
        assertThat(new SchemaUpdater().initFlyway()).isNotNull();
    }

    @Test
    void should_invoke_Flyway_migration() {
        updater.init(null);

        assertThat(flyway.invoked).isTrue();
    }

    private class FlywayConfigurationMock extends FluentConfiguration {
        @Override
        public Flyway load() {
            return flyway;
        }

        @Override
        public FluentConfiguration dataSource(DataSource dataSource) {
            // Skip inspection of the datasource, as we don't have any.
            return this;
        }
    }

    private static class FlywayMock extends Flyway {
        boolean invoked = false;

        public FlywayMock(Configuration configuration) {
            super(configuration);
        }

        @Override
        public MigrateResult migrate() throws FlywayException {
            invoked = true;
            return new MigrateResult("whatever", "whatever", "whatever", "PostgreSQL");
        }
    }
}

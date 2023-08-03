package it.mulders.polly.infra.database;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

@ApplicationScoped
public class SchemaUpdater {
    private static final Logger logger = Logger.getLogger(SchemaUpdater.class.getName());

    @Resource(name = "jdbc/polly-ds")
    private DataSource dataSource;

    @SuppressWarnings("java:S1172") // "Unused method parameters should be removed"
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        var locations = determineFlywayLocations(System.getenv());
        logger.log(Level.INFO, "Reading database migrations from {0}", locations);
        final Flyway flyway =
                Flyway.configure().locations(locations).dataSource(dataSource).load();
        try {
            logger.log(Level.INFO, "Migrating database schema...");
            flyway.migrate();
            logger.log(Level.INFO, "Migrating database schema done!");
        } catch (final FlywayException fe) {
            logger.log(Level.SEVERE, "Failed to migrate database schema", fe);
        }
    }

    protected String[] determineFlywayLocations(Map<String, String> environmentVariables) {
        var flywayLocations = environmentVariables.get("FLYWAY_LOCATIONS");
        if (flywayLocations == null) {
            logger.log(Level.INFO, "Environment variable FLYWAY_LOCATIONS not found, using default");
            flywayLocations = "classpath:db/migration,classpath:db/migration-postgresql";
        }
        return flywayLocations.split(",");
    }
}

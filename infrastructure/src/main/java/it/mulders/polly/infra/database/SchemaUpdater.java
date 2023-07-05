package it.mulders.polly.infra.database;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
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
        final Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        try {
            logger.log(Level.INFO, "Migrating database schema...");
            flyway.migrate();
            logger.log(Level.INFO, "Migrating database schema done!");
        } catch (final FlywayException fe) {
            logger.log(Level.SEVERE, "Failed to migrate database schema", fe);
        }
    }
}

package it.mulders.polly.infra.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.util.Map;
import java.util.function.Function;
import org.assertj.core.api.WithAssertions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.Location;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @param <Int> repository type
 * @param <Impl> JPA-based repository type
 */
@Testcontainers
public abstract class AbstractJpaRepositoryTest<Int, Impl extends Int> implements WithAssertions {
    @Container
    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:14.6-alpine");

    protected EntityManager entityManager;
    protected Int repository;

    protected void prepare(Function<EntityManager, Impl> entityManagerCreator) {
        var username = POSTGRESQL_CONTAINER.getUsername();
        var url = POSTGRESQL_CONTAINER.getJdbcUrl();
        var driver = POSTGRESQL_CONTAINER.getDriverClassName();
        var password = POSTGRESQL_CONTAINER.getPassword();

        var props = Map.of(
                "jakarta.persistence.jdbc.user", username,
                "jakarta.persistence.jdbc.url", url,
                "jakarta.persistence.jdbc.driver", driver,
                "jakarta.persistence.jdbc.password", password);

        var flyway = Flyway.configure()
                .locations(new Location("filesystem:src/main/resources/db/migration"))
                .dataSource(url, username, password)
                .load();
        try {
            flyway.migrate();
        } catch (final FlywayException fe) {
            fail("Failed to migrate database schema", fe);
        }

        var emf = Persistence.createEntityManagerFactory("test", props);
        this.entityManager = emf.createEntityManager(props);
        this.repository = entityManagerCreator.apply(this.entityManager);

        prepareHelperEntities(entityManager);
    }

    /**
     * If a JPA-repository test needs additional entities, we can't store them using JPA cascading relations.
     * Either EclipseLink fails to properly serialise UUID values (fixed in 4.0.1) or it cannot generate them
     * (EclipseLink 3.x does not implement JPA 3.1 features).
     *
     * This method acts as a hook for subclasses to create additional repositories, storing additional
     * entities.
     *
     * @param em The {@link EntityManager} that the helper repositories need.
     */
    protected void prepareHelperEntities(final EntityManager em) {}
}

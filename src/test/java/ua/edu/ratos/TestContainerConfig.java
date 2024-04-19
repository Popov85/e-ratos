package ua.edu.ratos;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

@TestConfiguration
public class TestContainerConfig {

    private static final String MYSQL_IMAGE_VERSION = "8.0.36";

    // Inject the locations as a list
    @Value("${spring.flyway.locations}")
    private String[] flywayLocations;


    @Bean
    @ServiceConnection
    @SuppressWarnings("resource")
    public MySQLContainer<?> container() {
        return new MySQLContainer<>("mysql:" + MYSQL_IMAGE_VERSION)
                .withUsername("test")
                .withPassword("test")
                .withDatabaseName("ratos3");
    }

    @Bean
    @SuppressWarnings("resource")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(
                        container().getJdbcUrl(),
                        container().getUsername(),
                        container().getPassword())
                .locations(flywayLocations)
                .cleanDisabled(false)
                .load();
    }
}
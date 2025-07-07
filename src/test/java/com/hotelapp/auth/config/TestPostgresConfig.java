package com.hotelapp.auth.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class TestPostgresConfig {

    public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("authdb")
            .withUsername("authuser")
            .withPassword("authpass");

    static {
        postgresContainer.start();
    }

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        return postgresContainer;
    }
}

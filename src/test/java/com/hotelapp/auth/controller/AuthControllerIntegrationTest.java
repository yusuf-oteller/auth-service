package com.hotelapp.auth.controller;

import com.hotelapp.auth.config.TestPostgresConfig;
import com.hotelapp.auth.dto.AuthRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestPostgresConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class AuthControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    @Container
    static PostgreSQLContainer<?> postgresContainer = TestPostgresConfig.postgresContainer;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @Order(1)
    void shouldRegisterUser() throws Exception {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("inttest@example.com");
        dto.setPassword("password");

        String url = "http://localhost:" + port + "/api/v1/auth/register";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }
}

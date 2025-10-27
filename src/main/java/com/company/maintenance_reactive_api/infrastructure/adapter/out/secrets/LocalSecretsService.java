package com.company.maintenance_reactive_api.infrastructure.adapter.out.secrets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalSecretsService {

    private final Mono<String> databaseSecret;
    private final ObjectMapper objectMapper;

    public Mono<Map<String, String>> getDatabaseCredentials() {
        return databaseSecret
                .flatMap(secretJson -> {
                    try {
                        Map<String, String> credentials = objectMapper.readValue(
                                secretJson,
                                new TypeReference<Map<String, String>>() {}
                        );
                        log.info("Database credentials loaded successfully");
                        return Mono.just(credentials);
                    } catch (Exception e) {
                        log.error("Error parsing secret JSON: {}", e.getMessage());
                        return Mono.just(getFallbackCredentials());
                    }
                });
    }

    private Map<String, String> getFallbackCredentials() {
        return Map.of(
                "username", "local-user",
                "password", "local-pass",
                "host", "localhost",
                "port", "5432",
                "database", "maintenance_db"
        );
    }
}

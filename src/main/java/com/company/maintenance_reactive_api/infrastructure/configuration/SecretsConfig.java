package com.company.maintenance_reactive_api.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class SecretsConfig {

    private final Environment environment;

    public SecretsConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Mono<String> databaseSecret() {
        String secret = environment.getProperty("aws.secrets.database", getDefaultSecret());
        log.info("🔧 Using database secret: {}", secret.replaceAll(".", "*")); // Mask secret
        return Mono.just(secret);
    }

    private String getDefaultSecret() {
        return """
            {
                "username": "test-user",
                "password": "test-pass", 
                "host": "localhost",
                "port": "5432",
                "database": "maintenance_db"
            }
            """;
    }
}
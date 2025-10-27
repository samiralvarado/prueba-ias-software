package com.company.maintenance_reactive_api.infrastructure.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class JWTConfig {

    @Value("${jwt.secret:mySuperSecretKeyForJWTTokenGenerationInMaintenanceApp2024}")
    private String jwtSecret;

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes = Base64.getEncoder().encode(jwtSecret.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

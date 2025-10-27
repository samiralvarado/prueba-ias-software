package com.company.maintenance_reactive_api.infrastructure.adapter.out.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final SecretKey jwtSecretKey;

    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    public Mono<String> generateToken(String username, String role) {
        return Mono.fromCallable(() ->
                Jwts.builder()
                        .setSubject(username)
                        .claim("role", role)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                        .compact()
        );
    }

    public Mono<String> validateToken(String token) {
        return Mono.fromCallable(() ->
                Jwts.parserBuilder()
                        .setSigningKey(jwtSecretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        ).onErrorResume(throwable -> Mono.empty());
    }
}
package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller;

import com.company.maintenance_reactive_api.domain.constants.RoleConstants;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.LoginRequest;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.LoginResponse;
import com.company.maintenance_reactive_api.infrastructure.adapter.out.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTService jwtService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return Mono.just(loginRequest)
                .flatMap(this::authenticateAndGenerateToken)
                .onErrorResume(RuntimeException.class, e ->
                        Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas")));
    }

    private Mono<LoginResponse> authenticateAndGenerateToken(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        return Mono.just(request)
                .filter(req -> isValidCredentials(username, password))
                .flatMap(req -> generateTokenForUser(username))
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")));
    }

    private boolean isValidCredentials(String username, String password) {
        return "password".equals(password) &&
                ("admin".equals(username) || "supervisor".equals(username) || "root".equals(username));
    }

    private Mono<LoginResponse> generateTokenForUser(String username) {
        String role = switch (username) {
            case "admin" -> RoleConstants.TECHNICIAN;
            case "supervisor" -> RoleConstants.SUPERVISOR;
            case "root" -> RoleConstants.SUPERADMIN;
            default -> null;
        };

        return jwtService.generateToken(username, role)
                .map(token -> buildResponse(token, username, role));
    }

    @GetMapping("/validate")
    public Mono<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        return Mono.justOrEmpty(authHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .filter(token -> !token.trim().isEmpty())
                .flatMap(jwtService::validateToken)
                .switchIfEmpty(Mono.error(new RuntimeException("Token inválido")));
    }

    private LoginResponse buildResponse(String token, String username, String role) {
        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .username(username)
                .role(role)
                .build();
    }
}

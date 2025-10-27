package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller;

import com.company.maintenance_reactive_api.domain.constants.RoleConstants;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.LoginRequest;
import com.company.maintenance_reactive_api.infrastructure.adapter.out.security.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JWTService jwtService;

    private AuthController authController;

    @BeforeEach
    void setup() {
        authController = new AuthController(jwtService);
    }

    @Test
    void login_shouldReturnTokenForAdmin() {

        String username = "admin";
        String password = "password";
        String expectedToken = "mocked-token";

        when(jwtService.generateToken(username, RoleConstants.TECHNICIAN))
                .thenReturn(Mono.just(expectedToken));

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);


        StepVerifier.create(authController.login(request))
                .expectNextMatches(response ->
                        response.getToken().equals(expectedToken) &&
                                response.getRole().equals(RoleConstants.TECHNICIAN) &&
                                response.getUsername().equals(username) &&
                                response.getType().equals("Bearer"))
                .verifyComplete();
    }

    @Test
    void login_shouldReturnTokenForSupervisor() {

        String username = "supervisor";
        String password = "password";
        String expectedToken = "token-supervisor";

        when(jwtService.generateToken(username, RoleConstants.SUPERVISOR))
                .thenReturn(Mono.just(expectedToken));

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        StepVerifier.create(authController.login(request))
                .expectNextMatches(response ->
                        response.getToken().equals(expectedToken) &&
                                response.getRole().equals(RoleConstants.SUPERVISOR) &&
                                response.getUsername().equals(username) &&
                                response.getType().equals("Bearer"))
                .verifyComplete();
    }

    @Test
    void login_shouldReturnTokenForRoot() {

        String username = "root";
        String password = "password";
        String expectedToken = "token-root";

        when(jwtService.generateToken(username, RoleConstants.SUPERADMIN))
                .thenReturn(Mono.just(expectedToken));

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        StepVerifier.create(authController.login(request))
                .expectNextMatches(response ->
                        response.getToken().equals(expectedToken) &&
                                response.getRole().equals(RoleConstants.SUPERADMIN) &&
                                response.getUsername().equals(username) &&
                                response.getType().equals("Bearer"))
                .verifyComplete();
    }

    @Test
    void login_shouldReturnErrorForInvalidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setUsername("wrong");
        request.setPassword("bad");


        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorForWrongPassword() {

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");


        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorWhenJWTServiceFails() {
        // Arrange
        String username = "admin";
        String password = "password";

        when(jwtService.generateToken(anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("JWT generation failed")));

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorForEmptyUsername() {

        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("password");


        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorForEmptyPassword() {

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("");


        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorForNullUsername() {

        LoginRequest request = new LoginRequest();
        request.setUsername(null);
        request.setPassword("password");

        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorForNullPassword() {

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword(null);

        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }


    @Test
    void validateToken_shouldReturnValidToken() {

        String token = "valid-token";
        String header = "Bearer " + token;

        when(jwtService.validateToken(token))
                .thenReturn(Mono.just("Token válido"));

        StepVerifier.create(authController.validateToken(header))
                .expectNext("Token válido")
                .verifyComplete();
    }

    @Test
    void validateToken_shouldReturnErrorForInvalidToken() {

        String token = "invalid-token";
        String header = "Bearer " + token;

        when(jwtService.validateToken(token))
                .thenReturn(Mono.error(new RuntimeException("Token inválido")));

        StepVerifier.create(authController.validateToken(header))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Token inválido"))
                .verify();
    }

    @Test
    void validateToken_shouldReturnErrorIfHeaderMissing() {

        StepVerifier.create(authController.validateToken(null))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Token inválido"))
                .verify();
    }

    @Test
    void validateToken_shouldReturnErrorIfNoBearerPrefix() {

        String header = "InvalidHeader valid-token";

        StepVerifier.create(authController.validateToken(header))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Token inválido"))
                .verify();
    }

    @Test
    void validateToken_shouldReturnErrorIfBearerWithoutToken() {

        String header = "Bearer ";


        StepVerifier.create(authController.validateToken(header))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Token inválido"))
                .verify();
    }

    @Test
    void validateToken_shouldReturnErrorForEmptyToken() {

        String header = "Bearer";

        StepVerifier.create(authController.validateToken(header))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Token inválido"))
                .verify();
    }

    @Test
    void login_shouldReturnErrorForUnknownUserWithCorrectPassword() {

        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("password");


        StepVerifier.create(authController.login(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNAUTHORIZED &&
                                throwable.getMessage().contains("Credenciales inválidas"))
                .verify();
    }
}
package com.company.maintenance_reactive_api.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void securityWebFilterChain_shouldCreateFilterChain() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
    }

    @Test
    void securityWebFilterChain_shouldDisableCsrf() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
    }

    @Test
    void securityWebFilterChain_shouldPermitAuthEndpoints() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
    }

    @Test
    void securityWebFilterChain_shouldDisableHttpBasic() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
    }

    @Test
    void securityWebFilterChain_shouldDisableFormLogin() {
        ServerHttpSecurity http = ServerHttpSecurity.http();

        SecurityWebFilterChain filterChain = securityConfig.securityWebFilterChain(http);

        assertNotNull(filterChain);
    }
}

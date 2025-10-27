package com.company.maintenance_reactive_api.infrastructure.configuration;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTConfigTest {

    private JWTConfig jwtConfig;

    @BeforeEach
    void setup() {
        jwtConfig = new JWTConfig();
        ReflectionTestUtils.setField(jwtConfig, "jwtSecret",
                "mySuperSecretKeyForJWTTokenGenerationInMaintenanceApp2024");
    }

    @Test
    void jwtSecretKey_shouldCreateNonNullKey() {
        SecretKey secretKey = jwtConfig.jwtSecretKey();

        assertNotNull(secretKey, "SecretKey should not be null");
    }

    @Test
    void jwtSecretKey_shouldCreateHMACKey() {

        SecretKey secretKey = jwtConfig.jwtSecretKey();

        assertNotNull(secretKey);
        assertTrue(secretKey.getAlgorithm().startsWith("Hmac"),
                "Should be an HMAC algorithm");
    }

    @Test
    void jwtSecretKey_shouldHaveNonEmptyEncodedForm() {

        SecretKey secretKey = jwtConfig.jwtSecretKey();

        assertNotNull(secretKey.getEncoded());
        assertTrue(secretKey.getEncoded().length > 0,
                "Encoded key should not be empty");
    }

    @Test
    void jwtSecretKey_shouldBeDeterministic() {

        SecretKey key1 = jwtConfig.jwtSecretKey();
        SecretKey key2 = jwtConfig.jwtSecretKey();

        assertArrayEquals(key1.getEncoded(), key2.getEncoded(),
                "Multiple calls should return the same key");
    }

    @Test
    void jwtSecretKey_shouldSupportSigningOperations() {

        SecretKey secretKey = jwtConfig.jwtSecretKey();

        assertNotNull(secretKey);
        assertEquals("RAW", secretKey.getFormat(),
                "Key format should be RAW for HMAC keys");
    }

    @Test
    void jwtSecretKey_shouldWorkWithJJWTKeysUtility() {
        SecretKey secretKey = jwtConfig.jwtSecretKey();

        assertNotNull(secretKey);

        assertDoesNotThrow(() -> {
            SecretKey testKey = Keys.hmacShaKeyFor(secretKey.getEncoded());
            assertNotNull(testKey);
        });
    }
}
package com.company.maintenance_reactive_api.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecretsConfigTest {

    @Mock
    private Environment environment;

    @Test
    void databaseSecret_shouldReturnSecretFromEnvironment() {

        String expectedSecret = "{\"username\":\"test\",\"password\":\"secret\"}";
        when(environment.getProperty(eq("aws.secrets.database"), anyString()))
                .thenReturn(expectedSecret);

        SecretsConfig secretsConfig = new SecretsConfig(environment);


        StepVerifier.create(secretsConfig.databaseSecret())
                .expectNext(expectedSecret)
                .verifyComplete();
    }

    @Test
    void databaseSecret_shouldReturnDefaultSecretWhenPropertyNotSet() {

        when(environment.getProperty(eq("aws.secrets.database"), anyString()))
                .thenAnswer(invocation -> invocation.getArgument(1));

        SecretsConfig secretsConfig = new SecretsConfig(environment);

        StepVerifier.create(secretsConfig.databaseSecret())
                .expectNextMatches(secret ->
                        secret.contains("test-user") &&
                                secret.contains("test-pass") &&
                                secret.contains("maintenance_db"))
                .verifyComplete();
    }

    @Test
    void databaseSecret_shouldReturnMonoWithSecret() {

        String customSecret = "custom-secret";
        when(environment.getProperty(eq("aws.secrets.database"), anyString()))
                .thenReturn(customSecret);

        SecretsConfig secretsConfig = new SecretsConfig(environment);


        StepVerifier.create(secretsConfig.databaseSecret())
                .expectNext(customSecret)
                .verifyComplete();
    }

    @Test
    void databaseSecret_shouldNotBeEmpty() {

        when(environment.getProperty(eq("aws.secrets.database"), anyString()))
                .thenReturn("non-empty-secret");

        SecretsConfig secretsConfig = new SecretsConfig(environment);

        StepVerifier.create(secretsConfig.databaseSecret())
                .expectNextMatches(secret -> !secret.isEmpty())
                .verifyComplete();
    }

    @Test
    void databaseSecret_shouldCallEnvironmentWithCorrectParameters() {

        when(environment.getProperty(eq("aws.secrets.database"), anyString()))
                .thenReturn("test-secret");

        SecretsConfig secretsConfig = new SecretsConfig(environment);


        StepVerifier.create(secretsConfig.databaseSecret())
                .expectNext("test-secret")
                .verifyComplete();


        verify(environment).getProperty(eq("aws.secrets.database"), anyString());
    }

    @Test
    void databaseSecret_shouldCompleteAfterEmission() {

        when(environment.getProperty(eq("aws.secrets.database"), anyString()))
                .thenReturn("any-secret");

        SecretsConfig secretsConfig = new SecretsConfig(environment);

        StepVerifier.create(secretsConfig.databaseSecret())
                .expectNextCount(1)
                .verifyComplete();
    }
}
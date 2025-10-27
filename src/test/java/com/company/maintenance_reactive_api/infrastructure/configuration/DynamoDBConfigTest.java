package com.company.maintenance_reactive_api.infrastructure.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DynamoDBConfigTest {

    private DynamoDBConfig dynamoDBConfig;

    @BeforeEach
    void setup() {
        dynamoDBConfig = new DynamoDBConfig();

        ReflectionTestUtils.setField(dynamoDBConfig, "awsRegion", "us-east-1");
        ReflectionTestUtils.setField(dynamoDBConfig, "dynamoDbEndpoint", "http://localhost:4566");
    }

    @Test
    void dynamoDbAsyncClient_shouldCreateClient() {

        DynamoDbAsyncClient client = dynamoDBConfig.dynamoDbAsyncClient();


        assertNotNull(client);
        assertEquals("dynamodb", client.serviceName());
    }

    @Test
    void dynamoDbEnhancedAsyncClient_shouldCreateEnhancedClient() {

        DynamoDbAsyncClient asyncClient = dynamoDBConfig.dynamoDbAsyncClient();

        DynamoDbEnhancedAsyncClient enhancedClient = dynamoDBConfig.dynamoDbEnhancedAsyncClient(asyncClient);

        assertNotNull(enhancedClient);
    }

    @Test
    void dynamoDbEnhancedAsyncClient_shouldNotReturnNull() {

        DynamoDbAsyncClient asyncClient = dynamoDBConfig.dynamoDbAsyncClient();

        DynamoDbEnhancedAsyncClient enhancedClient = dynamoDBConfig.dynamoDbEnhancedAsyncClient(asyncClient);

        assertNotNull(enhancedClient);
    }

    @Test
    void dynamoDbAsyncClient_shouldBeNonNull() {

        assertNotNull(dynamoDBConfig.dynamoDbAsyncClient());
    }

    @Test
    void dynamoDbEnhancedAsyncClient_shouldAcceptNonNullAsyncClient() {

        DynamoDbAsyncClient asyncClient = dynamoDBConfig.dynamoDbAsyncClient();
        assertNotNull(asyncClient);

        DynamoDbEnhancedAsyncClient enhancedClient = dynamoDBConfig.dynamoDbEnhancedAsyncClient(asyncClient);

        assertNotNull(enhancedClient);
    }
}
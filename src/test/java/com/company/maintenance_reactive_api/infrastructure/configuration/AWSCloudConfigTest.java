package com.company.maintenance_reactive_api.infrastructure.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.regions.Region;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AWSCloudConfigTest {

    private AWSCloudConfig awsCloudConfig;

    @BeforeEach
    void setup() {
        awsCloudConfig = new AWSCloudConfig();
        ReflectionTestUtils.setField(awsCloudConfig, "awsRegion", "us-east-1");
    }

    @Test
    void awsRegion_shouldReturnConfiguredRegion() {

        Region region = awsCloudConfig.awsRegion();


        assertNotNull(region);
        assertEquals(Region.US_EAST_1, region);
    }

    @Test
    void awsRegion_shouldReturnValidRegion() {

        Region region = awsCloudConfig.awsRegion();


        assertNotNull(region);
        assertTrue(region.id().matches("[a-z]+-[a-z]+-\\d+"));
    }
}
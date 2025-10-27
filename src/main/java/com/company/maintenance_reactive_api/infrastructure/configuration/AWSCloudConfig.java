package com.company.maintenance_reactive_api.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AWSCloudConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    public Region awsRegion() {
        return Region.of(awsRegion);
    }
}
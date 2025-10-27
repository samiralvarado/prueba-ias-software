package com.company.maintenance_reactive_api.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@DynamoDbBean
public class Machine {
    private String id;
    private String name;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Machine(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
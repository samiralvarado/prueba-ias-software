package com.company.maintenance_reactive_api.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@DynamoDbBean
public class Maintenance {
    private String id;
    private String machineId;
    private MaintenanceType type;
    private String description;
    private MaintenanceStatus status;
    private String technicianId;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private LocalDateTime createdAt;

    public enum MaintenanceType {
        PREVENTIVE, CORRECTIVE
    }

    public enum MaintenanceStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public Maintenance(String id, String machineId, MaintenanceType type, String description) {
        this.id = id;
        this.machineId = machineId;
        this.type = type;
        this.description = description;
        this.status = MaintenanceStatus.SCHEDULED;
        this.createdAt = LocalDateTime.now();
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void completeMaintenance() {
        this.status = MaintenanceStatus.COMPLETED;
        this.completedDate = LocalDateTime.now();
    }

    public void startMaintenance() {
        this.status = MaintenanceStatus.IN_PROGRESS;
    }
}
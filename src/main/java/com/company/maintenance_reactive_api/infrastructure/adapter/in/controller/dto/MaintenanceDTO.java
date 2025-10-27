package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceDTO {
    private String id;
    private String machineId;
    private Maintenance.MaintenanceType type;
    private String description;
    private Maintenance.MaintenanceStatus status;
    private String technicianId;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private LocalDateTime createdAt;
}
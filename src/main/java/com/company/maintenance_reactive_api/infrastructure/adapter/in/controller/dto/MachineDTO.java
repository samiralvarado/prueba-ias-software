package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MachineDTO {
    private String id;
    private String name;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
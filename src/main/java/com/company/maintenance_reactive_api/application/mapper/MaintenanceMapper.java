package com.company.maintenance_reactive_api.application.mapper;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MaintenanceDTO;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceDTO toDTO(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }

        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setId(maintenance.getId());
        dto.setMachineId(maintenance.getMachineId());
        dto.setType(maintenance.getType());
        dto.setDescription(maintenance.getDescription());
        dto.setStatus(maintenance.getStatus());
        dto.setTechnicianId(maintenance.getTechnicianId());
        dto.setScheduledDate(maintenance.getScheduledDate());
        dto.setCompletedDate(maintenance.getCompletedDate());
        dto.setCreatedAt(maintenance.getCreatedAt());

        return dto;
    }

    public Maintenance toDomain(MaintenanceDTO dto) {
        if (dto == null) {
            return null;
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setId(dto.getId());
        maintenance.setMachineId(dto.getMachineId());
        maintenance.setType(dto.getType());
        maintenance.setDescription(dto.getDescription());
        maintenance.setStatus(dto.getStatus());
        maintenance.setTechnicianId(dto.getTechnicianId());
        maintenance.setScheduledDate(dto.getScheduledDate());
        maintenance.setCompletedDate(dto.getCompletedDate());

        return maintenance;
    }
}

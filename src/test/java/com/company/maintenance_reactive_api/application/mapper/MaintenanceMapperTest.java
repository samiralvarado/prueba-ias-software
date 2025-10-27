package com.company.maintenance_reactive_api.application.mapper;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MaintenanceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceMapperTest {

    @InjectMocks
    private MaintenanceMapper maintenanceMapper;

    @Test
    void toDTO_WhenMaintenanceIsNull_ShouldReturnNull() {
        MaintenanceDTO result = maintenanceMapper.toDTO(null);

        assertNull(result);
    }

    @Test
    void toDTO_WhenMaintenanceIsValid_ShouldMapCorrectly() {

        LocalDateTime now = LocalDateTime.now();
        Maintenance maintenance = new Maintenance();
        maintenance.setId("123");
        maintenance.setMachineId("machine-456");
        maintenance.setType(Maintenance.MaintenanceType.PREVENTIVE);
        maintenance.setDescription("Preventive maintenance");
        maintenance.setStatus(Maintenance.MaintenanceStatus.SCHEDULED);
        maintenance.setTechnicianId("tech-789");
        maintenance.setScheduledDate(now.plusDays(1));
        maintenance.setCompletedDate(now.plusDays(2));
        maintenance.setCreatedAt(now);

        MaintenanceDTO result = maintenanceMapper.toDTO(maintenance);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("machine-456", result.getMachineId());
        assertEquals(Maintenance.MaintenanceType.PREVENTIVE, result.getType());
        assertEquals("Preventive maintenance", result.getDescription());
        assertEquals(Maintenance.MaintenanceStatus.SCHEDULED, result.getStatus());
        assertEquals("tech-789", result.getTechnicianId());
        assertEquals(now.plusDays(1), result.getScheduledDate());
        assertEquals(now.plusDays(2), result.getCompletedDate());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    void toDomain_WhenDTOIsNull_ShouldReturnNull() {

        Maintenance result = maintenanceMapper.toDomain(null);
        assertNull(result);
    }

    @Test
    void toDomain_WhenDTOIsValid_ShouldMapCorrectly() {

        LocalDateTime now = LocalDateTime.now();
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setId("123");
        dto.setMachineId("machine-456");
        dto.setType(Maintenance.MaintenanceType.CORRECTIVE);
        dto.setDescription("Corrective maintenance");
        dto.setStatus(Maintenance.MaintenanceStatus.IN_PROGRESS);
        dto.setTechnicianId("tech-789");
        dto.setScheduledDate(now.plusDays(1));
        dto.setCompletedDate(now.plusDays(2));
        dto.setCreatedAt(now);

        Maintenance result = maintenanceMapper.toDomain(dto);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("machine-456", result.getMachineId());
        assertEquals(Maintenance.MaintenanceType.CORRECTIVE, result.getType());
        assertEquals("Corrective maintenance", result.getDescription());
        assertEquals(Maintenance.MaintenanceStatus.IN_PROGRESS, result.getStatus());
        assertEquals("tech-789", result.getTechnicianId());
        assertEquals(now.plusDays(1), result.getScheduledDate());
        assertEquals(now.plusDays(2), result.getCompletedDate());
    }
}

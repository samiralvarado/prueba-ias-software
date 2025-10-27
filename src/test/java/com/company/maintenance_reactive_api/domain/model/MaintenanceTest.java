package com.company.maintenance_reactive_api.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceTest {

    @Test
    void constructor_ShouldInitializeWithDefaultValues() {
        Maintenance maintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");


        assertEquals("1", maintenance.getId());
        assertEquals("machine-1", maintenance.getMachineId());
        assertEquals(Maintenance.MaintenanceType.PREVENTIVE, maintenance.getType());
        assertEquals("Preventive maintenance", maintenance.getDescription());
        assertEquals(Maintenance.MaintenanceStatus.SCHEDULED, maintenance.getStatus());
        assertNotNull(maintenance.getCreatedAt());
        assertNull(maintenance.getCompletedDate());
    }

    @Test
    void completeMaintenance_ShouldUpdateStatusAndCompletedDate() {
        Maintenance maintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");

        maintenance.completeMaintenance();

        assertEquals(Maintenance.MaintenanceStatus.COMPLETED, maintenance.getStatus());
        assertNotNull(maintenance.getCompletedDate());
    }

    @Test
    void startMaintenance_ShouldUpdateStatus() {
        Maintenance maintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");

        maintenance.startMaintenance();

        assertEquals(Maintenance.MaintenanceStatus.IN_PROGRESS, maintenance.getStatus());
    }

    @Test
    void getId_ShouldReturnId() {
        Maintenance maintenance = new Maintenance();
        maintenance.setId("123");

        assertEquals("123", maintenance.getId());
    }
}

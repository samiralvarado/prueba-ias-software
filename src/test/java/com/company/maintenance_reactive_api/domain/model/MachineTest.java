package com.company.maintenance_reactive_api.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {

    @Test
    void constructor_ShouldInitializeWithCorrectValues() {

        Machine machine = new Machine("machine-001", "Torno CNC", "Area A");


        assertEquals("machine-001", machine.getId());
        assertEquals("Torno CNC", machine.getName());
        assertEquals("Area A", machine.getLocation());
        assertNotNull(machine.getCreatedAt());
        assertNotNull(machine.getUpdatedAt());
    }

    @Test
    void updateTimestamp_ShouldUpdateUpdatedAt() throws InterruptedException {

        Machine machine = new Machine("machine-001", "Torno CNC", "Area A");
        LocalDateTime initialUpdateTime = machine.getUpdatedAt();


        Thread.sleep(1);
        machine.updateTimestamp();


        assertTrue(machine.getUpdatedAt().isAfter(initialUpdateTime));
    }

    @Test
    void noArgsConstructor_ShouldCreateInstance() {

        Machine machine = new Machine();


        assertNotNull(machine);
        assertNull(machine.getId());
        assertNull(machine.getName());
        assertNull(machine.getLocation());
        assertNull(machine.getCreatedAt());
        assertNull(machine.getUpdatedAt());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {

        Machine machine = new Machine();
        LocalDateTime now = LocalDateTime.now();


        machine.setId("machine-001");
        machine.setName("Torno CNC");
        machine.setLocation("Area A");
        machine.setCreatedAt(now);
        machine.setUpdatedAt(now);


        assertEquals("machine-001", machine.getId());
        assertEquals("Torno CNC", machine.getName());
        assertEquals("Area A", machine.getLocation());
        assertEquals(now, machine.getCreatedAt());
        assertEquals(now, machine.getUpdatedAt());
    }
}
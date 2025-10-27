package com.company.maintenance_reactive_api.application.mapper;

import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MachineDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MachineMapperTest {

    @InjectMocks
    private MachineMapper machineMapper;

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Machine machine = new Machine("machine-001", "Torno CNC", "Area A");
        machine.setCreatedAt(now);
        machine.setUpdatedAt(now);

        // Act & Assert usando Mono y StepVerifier
        Mono<MachineDTO> result = Mono.just(machine)
                .map(machineMapper::toDTO);

        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertNotNull(dto);
                    assertEquals("machine-001", dto.getId());
                    assertEquals("Torno CNC", dto.getName());
                    assertEquals("Area A", dto.getLocation());
                    assertEquals(now, dto.getCreatedAt());
                    assertEquals(now, dto.getUpdatedAt());
                })
                .verifyComplete();
    }

    @Test
    void toDTO_WithNullMachine_ShouldReturnNull() {
        // Act & Assert
        Mono<MachineDTO> result = Mono.justOrEmpty((Machine) null)
                .map(machineMapper::toDTO);

        StepVerifier.create(result)
                .verifyComplete(); // Flux vacío
    }

    @Test
    void toDomain_ShouldMapBasicFieldsCorrectly() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        MachineDTO dto = new MachineDTO();
        dto.setId("machine-001");
        dto.setName("Torno CNC");
        dto.setLocation("Area A");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        // Act & Assert
        Mono<Machine> result = Mono.just(dto)
                .map(machineMapper::toDomain);

        StepVerifier.create(result)
                .assertNext(machine -> {
                    assertNotNull(machine);
                    assertEquals("machine-001", machine.getId());
                    assertEquals("Torno CNC", machine.getName());
                    assertEquals("Area A", machine.getLocation());
                    // Los campos createdAt y updatedAt NO se mapean en toDomain
                    assertNull(machine.getCreatedAt());
                    assertNull(machine.getUpdatedAt());
                })
                .verifyComplete();
    }

    @Test
    void toDomain_WithNullDTO_ShouldReturnNull() {
        // Act & Assert
        Mono<Machine> result = Mono.justOrEmpty((MachineDTO) null)
                .map(machineMapper::toDomain);

        StepVerifier.create(result)
                .verifyComplete(); // Flux vacío
    }

    @Test
    void updateFromDTO_ShouldUpdateFieldsAndTimestamp() {
        // Arrange
        LocalDateTime originalTime = LocalDateTime.now().minusDays(1);
        Machine existingMachine = new Machine("machine-001", "Nombre Viejo", "Ubicación Vieja");
        existingMachine.setCreatedAt(originalTime);
        existingMachine.setUpdatedAt(originalTime);

        MachineDTO dto = new MachineDTO();
        dto.setName("Nombre Nuevo");
        dto.setLocation("Ubicación Nueva");

        // Act & Assert
        Mono<Machine> result = Mono.just(existingMachine)
                .map(machine -> machineMapper.updateFromDTO(machine, dto));

        StepVerifier.create(result)
                .assertNext(updatedMachine -> {
                    assertNotNull(updatedMachine);
                    assertEquals("machine-001", updatedMachine.getId());
                    assertEquals("Nombre Nuevo", updatedMachine.getName());
                    assertEquals("Ubicación Nueva", updatedMachine.getLocation());
                    assertEquals(originalTime, updatedMachine.getCreatedAt());
                    assertTrue(updatedMachine.getUpdatedAt().isAfter(originalTime));
                })
                .verifyComplete();
    }

    @Test
    void updateFromDTO_WithPartialNullValues_ShouldUpdateOnlyNonNullFields() {
        // Arrange
        LocalDateTime originalTime = LocalDateTime.now().minusSeconds(1); // ⏰ Un segundo en el pasado
        Machine existingMachine = new Machine("machine-001", "Nombre Original", "Ubicación Original");
        existingMachine.setCreatedAt(originalTime);
        existingMachine.setUpdatedAt(originalTime);

        MachineDTO dto = new MachineDTO();
        dto.setName("Nuevo Nombre");

        Mono<Machine> result = Mono.just(existingMachine)
                .map(machine -> machineMapper.updateFromDTO(machine, dto));

        StepVerifier.create(result)
                .assertNext(updatedMachine -> {
                    assertEquals("Nuevo Nombre", updatedMachine.getName());
                    assertEquals("Ubicación Original", updatedMachine.getLocation());
                    assertTrue(updatedMachine.getUpdatedAt().isAfter(originalTime),
                            "updatedAt debería ser posterior al tiempo original. Original: " + originalTime + ", Actual: " + updatedMachine.getUpdatedAt());
                })
                .verifyComplete();
    }

    @Test
    void updateFromDTO_WithNullMachine_ShouldReturnNull() {

        MachineDTO dto = new MachineDTO();
        dto.setName("Nuevo Nombre");


        Mono<Machine> result = Mono.justOrEmpty((Machine) null)
                .map(machine -> machineMapper.updateFromDTO(machine, dto));

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void updateFromDTO_WithNullDTO_ShouldReturnOriginalMachine() {

        Machine existingMachine = new Machine("machine-001", "Nombre Original", "Ubicación Original");
        LocalDateTime originalUpdatedAt = existingMachine.getUpdatedAt();


        Mono<Machine> result = Mono.just(existingMachine)
                .map(machine -> machineMapper.updateFromDTO(machine, null));

        StepVerifier.create(result)
                .assertNext(returnedMachine -> {
                    assertSame(existingMachine, returnedMachine);
                    assertEquals("Nombre Original", returnedMachine.getName());
                    assertEquals("Ubicación Original", returnedMachine.getLocation());
                    assertEquals(originalUpdatedAt, returnedMachine.getUpdatedAt());
                })
                .verifyComplete();
    }

    @Test
    void updateFromDTO_WithEmptyDTO_ShouldUpdateTimestampOnly() {

        LocalDateTime originalTime = LocalDateTime.now().minusHours(1);
        Machine existingMachine = new Machine("machine-001", "Nombre Original", "Ubicación Original");
        existingMachine.setCreatedAt(originalTime);
        existingMachine.setUpdatedAt(originalTime);

        MachineDTO emptyDTO = new MachineDTO();


        Mono<Machine> result = Mono.just(existingMachine)
                .map(machine -> machineMapper.updateFromDTO(machine, emptyDTO));

        StepVerifier.create(result)
                .assertNext(updatedMachine -> {
                    assertEquals("Nombre Original", updatedMachine.getName());
                    assertEquals("Ubicación Original", updatedMachine.getLocation());
                    assertTrue(updatedMachine.getUpdatedAt().isAfter(originalTime));
                })
                .verifyComplete();
    }

    @Test
    void mapperMethods_ShouldBeReactiveCompatible() {

        Machine machine = new Machine("test-id", "Test Machine", "Test Location");
        machine.setCreatedAt(LocalDateTime.now());
        machine.setUpdatedAt(LocalDateTime.now());

        StepVerifier.create(
                        Mono.just(machine)
                                .map(machineMapper::toDTO)
                                .flatMap(mappedDTO -> Mono.just(mappedDTO)
                                        .map(machineMapper::toDomain))
                )
                .assertNext(resultMachine -> {

                    assertEquals("test-id", resultMachine.getId());
                    assertEquals("Test Machine", resultMachine.getName());
                    assertEquals("Test Location", resultMachine.getLocation());

                    assertNull(resultMachine.getCreatedAt());
                    assertNull(resultMachine.getUpdatedAt());
                })
                .verifyComplete();
    }}
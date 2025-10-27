package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller;

import com.company.maintenance_reactive_api.application.mapper.MachineMapper;
import com.company.maintenance_reactive_api.application.usecase.ListMachinesUseCase;
import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.MachineController;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MachineDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MachineControllerTest {

    @Mock
    private ListMachinesUseCase listMachinesUseCase;

    @Mock
    private MachineMapper machineMapper;

    @InjectMocks
    private MachineController machineController;

    private Machine createTestMachine(String id, String name, String location) {
        Machine machine = new Machine(id, name, location);
        machine.setCreatedAt(LocalDateTime.now());
        machine.setUpdatedAt(LocalDateTime.now());
        return machine;
    }

    private MachineDTO createTestMachineDTO(String id, String name, String location) {
        MachineDTO dto = new MachineDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setLocation(location);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void getAllMachines_ShouldReturnMachines() {
        Machine m1 = createTestMachine("1", "Torno", "A");
        Machine m2 = createTestMachine("2", "Fresadora", "B");

        MachineDTO dto1 = createTestMachineDTO("1", "Torno", "A");
        MachineDTO dto2 = createTestMachineDTO("2", "Fresadora", "B");

        when(listMachinesUseCase.execute()).thenReturn(Flux.just(m1, m2));
        when(machineMapper.toDTO(m1)).thenReturn(dto1);
        when(machineMapper.toDTO(m2)).thenReturn(dto2);

        StepVerifier.create(machineController.getAllMachines())
                .expectNext(dto1, dto2)
                .verifyComplete();
    }
}

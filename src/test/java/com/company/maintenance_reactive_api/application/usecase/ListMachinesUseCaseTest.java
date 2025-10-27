package com.company.maintenance_reactive_api.application.usecase;

import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.domain.repository.MachineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListMachinesUseCaseTest {

    @Mock
    private MachineRepository machineRepository;

    @InjectMocks
    private ListMachinesUseCase listMachinesUseCase;

    private Machine createTestMachine(String id, String name, String location) {
        Machine machine = new Machine(id, name, location);
        machine.setCreatedAt(LocalDateTime.now());
        machine.setUpdatedAt(LocalDateTime.now());
        return machine;
    }

    @Test
    void execute_WhenMachinesExist_ShouldReturnFluxOfMachines() {
        Machine machine1 = createTestMachine("machine-001", "Torno CNC", "Area A");
        Machine machine2 = createTestMachine("machine-002", "Fresadora", "Area B");

        when(machineRepository.findAll()).thenReturn(Flux.just(machine1, machine2));

        Flux<Machine> result = listMachinesUseCase.execute();

        StepVerifier.create(result)
                .expectNext(machine1)
                .expectNext(machine2)
                .verifyComplete();
    }

    @Test
    void execute_WhenNoMachines_ShouldReturnEmptyFlux() {
        when(machineRepository.findAll()).thenReturn(Flux.empty());

        Flux<Machine> result = listMachinesUseCase.execute();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void execute_WhenRepositoryThrowsError_ShouldReturnEmptyFlux() {
        when(machineRepository.findAll()).thenReturn(Flux.error(new RuntimeException("DB Connection failed")));

        Flux<Machine> result = listMachinesUseCase.execute();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}
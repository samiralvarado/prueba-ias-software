package com.company.maintenance_reactive_api.application.usecase;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.domain.repository.MaintenanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMaintenanceUseCaseTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private CreateMaintenanceUseCase createMaintenanceUseCase;

    @Test
    void execute_WhenMaintenanceIsValid_ShouldReturnSavedMaintenance() {

        Maintenance maintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");
        Maintenance savedMaintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");

        when(maintenanceRepository.save(any(Maintenance.class)))
                .thenReturn(Mono.just(savedMaintenance));


        StepVerifier.create(createMaintenanceUseCase.execute(maintenance))
                .expectNext(savedMaintenance)
                .verifyComplete();
    }

    @Test
    void execute_WhenRepositoryThrowsError_ShouldPropagateError() {

        Maintenance maintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");
        RuntimeException exception = new RuntimeException("Database error");

        when(maintenanceRepository.save(any(Maintenance.class)))
                .thenReturn(Mono.error(exception));


        StepVerifier.create(createMaintenanceUseCase.execute(maintenance))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Failed to create maintenance"))
                .verify();
    }

    @Test
    void execute_WhenMaintenanceIsNull_ShouldReturnIllegalArgumentException() {

        StepVerifier.create(createMaintenanceUseCase.execute(null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void execute_WhenMaintenanceIsNull_ShouldReturnErrorMessage() {

        StepVerifier.create(createMaintenanceUseCase.execute(null))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Maintenance cannot be null"))
                .verify();
    }
}
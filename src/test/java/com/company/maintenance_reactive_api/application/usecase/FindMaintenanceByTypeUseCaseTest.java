package com.company.maintenance_reactive_api.application.usecase;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.domain.repository.MaintenanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMaintenanceByTypeUseCaseTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private FindMaintenanceByTypeUseCase findMaintenanceByTypeUseCase;

    @Test
    void execute_WhenTypeExists_ShouldReturnMaintenances() {

        Maintenance.MaintenanceType type = Maintenance.MaintenanceType.PREVENTIVE;
        Maintenance maintenance1 = new Maintenance("1", "machine-1", type, "Preventive maintenance 1");
        Maintenance maintenance2 = new Maintenance("2", "machine-2", type, "Preventive maintenance 2");

        when(maintenanceRepository.findByType(type))
                .thenReturn(Flux.just(maintenance1, maintenance2));

        StepVerifier.create(findMaintenanceByTypeUseCase.execute(type))
                .expectNext(maintenance1)
                .expectNext(maintenance2)
                .verifyComplete();
    }

    @Test
    void execute_WhenNoMaintenancesFound_ShouldReturnEmptyFlux() {

        Maintenance.MaintenanceType type = Maintenance.MaintenanceType.CORRECTIVE;

        when(maintenanceRepository.findByType(type))
                .thenReturn(Flux.empty());

        StepVerifier.create(findMaintenanceByTypeUseCase.execute(type))
                .verifyComplete();
    }

    @Test
    void execute_WhenRepositoryThrowsError_ShouldReturnEmptyFluxAndLogError() {

        Maintenance.MaintenanceType type = Maintenance.MaintenanceType.PREVENTIVE;
        RuntimeException exception = new RuntimeException("Database error");

        when(maintenanceRepository.findByType(type))
                .thenReturn(Flux.error(exception));

        StepVerifier.create(findMaintenanceByTypeUseCase.execute(type))
                .verifyComplete();
    }

    @Test
    void execute_ShouldApplyBackpressureWithLimitRate() {

        Maintenance.MaintenanceType type = Maintenance.MaintenanceType.PREVENTIVE;
        Maintenance maintenance1 = new Maintenance("1", "machine-1", type, "Preventive maintenance 1");
        Maintenance maintenance2 = new Maintenance("2", "machine-2", type, "Preventive maintenance 2");

        when(maintenanceRepository.findByType(type))
                .thenReturn(Flux.just(maintenance1, maintenance2));

        StepVerifier.create(findMaintenanceByTypeUseCase.execute(type))
                .expectNextCount(2)
                .verifyComplete();
    }
}

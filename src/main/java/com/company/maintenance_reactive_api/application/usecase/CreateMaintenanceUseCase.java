package com.company.maintenance_reactive_api.application.usecase;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.domain.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateMaintenanceUseCase {

    private final MaintenanceRepository maintenanceRepository;

    public Mono<Maintenance> execute(Maintenance maintenance) {
        return Mono.justOrEmpty(maintenance)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Maintenance cannot be null")))
                .flatMap(this::saveMaintenance);
    }

    private Mono<Maintenance> saveMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance)
                .onErrorResume(throwable -> {
                    System.err.println("Error creating maintenance: " + throwable.getMessage());
                    return Mono.error(new RuntimeException("Failed to create maintenance"));
                });
    }
}

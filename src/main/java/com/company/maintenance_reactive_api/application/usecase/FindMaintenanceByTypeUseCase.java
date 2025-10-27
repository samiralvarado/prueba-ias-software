package com.company.maintenance_reactive_api.application.usecase;


import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.domain.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FindMaintenanceByTypeUseCase {

    private final MaintenanceRepository maintenanceRepository;

    public Flux<Maintenance> execute(Maintenance.MaintenanceType type) {
        return maintenanceRepository.findByType(type)
                .limitRate(50)
                .onErrorResume(throwable -> {
                    System.err.println("Error finding maintenance by type: " + throwable.getMessage());
                    return Flux.empty();
                });
    }
}

package com.company.maintenance_reactive_api.application.usecase;

import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.domain.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FindMachineByIdUseCase {

    private final MachineRepository machineRepository;

    public Mono<Machine> execute(String id) {
        return machineRepository.findById(id)
                .onErrorResume(throwable -> {
                    System.err.println("Error finding machine by id: " + id + " - " + throwable.getMessage());
                    return Mono.error(new RuntimeException("Database error while searching for machine"));
                });
    }
}

package com.company.maintenance_reactive_api.application.usecase;


import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.domain.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ListMachinesUseCase {

    private final MachineRepository machineRepository;

    public Flux<Machine> execute() {
        return machineRepository.findAll()
                .limitRate(100)
                .onErrorResume(throwable -> {
                    System.err.println("Error listing machines: " + throwable.getMessage());
                    return Flux.empty();
                });
    }
}
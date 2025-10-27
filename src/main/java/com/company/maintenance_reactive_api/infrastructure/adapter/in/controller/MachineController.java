package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller;

import com.company.maintenance_reactive_api.application.mapper.MachineMapper;
import com.company.maintenance_reactive_api.application.usecase.FindMachineByIdUseCase;
import com.company.maintenance_reactive_api.application.usecase.ListMachinesUseCase;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MachineDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final ListMachinesUseCase listMachinesUseCase;
    private final FindMachineByIdUseCase findMachineByIdUseCase;
    private final MachineMapper machineMapper;

    public MachineController(ListMachinesUseCase listMachinesUseCase, MachineMapper machineMapper,FindMachineByIdUseCase findMachineByIdUseCase) {
        this.listMachinesUseCase = listMachinesUseCase;
        this.machineMapper = machineMapper;
        this.findMachineByIdUseCase = findMachineByIdUseCase;
    }

    @GetMapping
    public Flux<MachineDTO> getAllMachines() {
        return listMachinesUseCase.execute()
                .map(machineMapper::toDTO)
                .onErrorResume(throwable -> {
                    System.err.println("Error in controller: " + throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/{id}")
    public Mono<MachineDTO> getMachineById(@PathVariable String id) {
        return findMachineByIdUseCase.execute(id)
                .map(machineMapper::toDTO)
                .onErrorResume(throwable -> {
                    System.err.println("Error getting machine by id: " + throwable.getMessage());
                    return Mono.empty();
                });
    }
}

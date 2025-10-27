package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller;

import com.company.maintenance_reactive_api.application.mapper.MaintenanceMapper;
import com.company.maintenance_reactive_api.application.usecase.CreateMaintenanceUseCase;
import com.company.maintenance_reactive_api.application.usecase.FindMaintenanceByTypeUseCase;
import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MaintenanceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final CreateMaintenanceUseCase createMaintenanceUseCase;
    private final FindMaintenanceByTypeUseCase findMaintenanceByTypeUseCase;
    private final MaintenanceMapper maintenanceMapper;


    @PostMapping
    public Mono<MaintenanceDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        return createMaintenanceUseCase.execute(maintenanceMapper.toDomain(maintenanceDTO))
                .map(maintenanceMapper::toDTO);
    }

    @GetMapping("/type/{type}")
    public Flux<MaintenanceDTO> getMaintenancesByType(@PathVariable Maintenance.MaintenanceType type) {
        return findMaintenanceByTypeUseCase.execute(type)
                .map(maintenanceMapper::toDTO);
    }

    @PutMapping("/{id}")
    public Mono<MaintenanceDTO> updateMaintenance(@PathVariable String id, @RequestBody MaintenanceDTO maintenanceDTO) {
        maintenanceDTO.setId(id);
        return createMaintenanceUseCase.execute(maintenanceMapper.toDomain(maintenanceDTO))
                .map(maintenanceMapper::toDTO);
    }
}
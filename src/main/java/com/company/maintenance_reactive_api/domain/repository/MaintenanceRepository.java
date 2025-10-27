package com.company.maintenance_reactive_api.domain.repository;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface MaintenanceRepository  {

    Mono<Maintenance> findById(String id);
    Flux<Maintenance> findAll();
    Flux<Maintenance> findByType(Maintenance.MaintenanceType type);
    Flux<Maintenance> findByMachineId(String machineId);
    Mono<Maintenance> save(Maintenance maintenance);
    Mono<Void> deleteById(String id);

}

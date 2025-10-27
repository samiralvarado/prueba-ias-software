package com.company.maintenance_reactive_api.domain.repository;

import com.company.maintenance_reactive_api.domain.model.Machine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MachineRepository {

    Mono<Machine> findById(String id);
    Flux<Machine> findAll();
    Mono<Machine> save(Machine machine);
    Mono<Void> deleteById(String id);

}

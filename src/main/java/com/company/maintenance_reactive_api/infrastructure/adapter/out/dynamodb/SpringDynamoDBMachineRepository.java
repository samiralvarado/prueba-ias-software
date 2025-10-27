package com.company.maintenance_reactive_api.infrastructure.adapter.out.dynamodb;

import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.domain.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

@Repository
@RequiredArgsConstructor
public class SpringDynamoDBMachineRepository implements MachineRepository {

    private final DynamoDbAsyncTable<Machine> machineTable;

    @Override
    public Mono<Machine> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(machineTable.getItem(key))
                .onErrorResume(throwable -> {
                    System.err.println("Error finding machine by id: " + throwable.getMessage());
                    return Mono.empty();
                });
    }

    @Override
    public Flux<Machine> findAll() {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().build();
        return Flux.from(machineTable.scan(scanRequest).items())
                .onErrorResume(throwable -> {
                    System.err.println("Error listing machines: " + throwable.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public Mono<Machine> save(Machine machine) {
        return Mono.fromFuture(machineTable.putItem(machine))
                .thenReturn(machine)
                .onErrorResume(throwable -> {
                    System.err.println("Error saving machine: " + throwable.getMessage());
                    return Mono.error(new RuntimeException("Failed to save machine"));
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(machineTable.deleteItem(key))
                .then()
                .onErrorResume(throwable -> {
                    System.err.println("Error deleting machine: " + throwable.getMessage());
                    return Mono.error(new RuntimeException("Failed to delete machine"));
                });
    }
}
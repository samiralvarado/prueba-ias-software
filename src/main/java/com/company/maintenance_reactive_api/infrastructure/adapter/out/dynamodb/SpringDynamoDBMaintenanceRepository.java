package com.company.maintenance_reactive_api.infrastructure.adapter.out.dynamodb;

import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.domain.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

@Repository
@RequiredArgsConstructor
public class SpringDynamoDBMaintenanceRepository implements MaintenanceRepository {

    private final DynamoDbAsyncTable<Maintenance> maintenanceTable;

    @Override
    public Mono<Maintenance> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(maintenanceTable.getItem(key))
                .onErrorResume(throwable -> {
                    System.err.println("Error finding maintenance by id: " + throwable.getMessage());
                    return Mono.empty();
                });
    }

    @Override
    public Flux<Maintenance> findAll() {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().build();
        return Flux.from(maintenanceTable.scan(scanRequest).items())
                .onErrorResume(throwable -> {
                    System.err.println("Error listing maintenances: " + throwable.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public Flux<Maintenance> findByType(Maintenance.MaintenanceType type) {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().build();
        return Flux.from(maintenanceTable.scan(scanRequest).items())
                .filter(maintenance -> maintenance.getType() == type)
                .onErrorResume(throwable -> {
                    System.err.println("Error finding maintenance by type: " + throwable.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public Flux<Maintenance> findByMachineId(String machineId) {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().build();
        return Flux.from(maintenanceTable.scan(scanRequest).items())
                .filter(maintenance -> machineId.equals(maintenance.getMachineId()))
                .onErrorResume(throwable -> {
                    System.err.println("Error finding maintenance by machineId: " + throwable.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public Mono<Maintenance> save(Maintenance maintenance) {
        return Mono.fromFuture(maintenanceTable.putItem(maintenance))
                .thenReturn(maintenance)
                .onErrorResume(throwable -> {
                    System.err.println("Error saving maintenance: " + throwable.getMessage());
                    return Mono.error(new RuntimeException("Failed to save maintenance"));
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(maintenanceTable.deleteItem(key))
                .then()
                .onErrorResume(throwable -> {
                    System.err.println("Error deleting maintenance: " + throwable.getMessage());
                    return Mono.error(new RuntimeException("Failed to delete maintenance"));
                });
    }
}
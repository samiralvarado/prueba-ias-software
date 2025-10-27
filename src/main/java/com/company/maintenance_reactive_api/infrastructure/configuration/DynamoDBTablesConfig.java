package com.company.maintenance_reactive_api.infrastructure.configuration;

import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.domain.model.Maintenance;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
@RequiredArgsConstructor
public class DynamoDBTablesConfig {

    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Bean
    public DynamoDbAsyncTable<Machine> machineTable() {
        return dynamoDbEnhancedAsyncClient.table("Machines", TableSchema.fromBean(Machine.class));
    }

    @Bean
    public DynamoDbAsyncTable<Maintenance> maintenanceTable() {
        return dynamoDbEnhancedAsyncClient.table("Maintenances", TableSchema.fromBean(Maintenance.class));
    }
}

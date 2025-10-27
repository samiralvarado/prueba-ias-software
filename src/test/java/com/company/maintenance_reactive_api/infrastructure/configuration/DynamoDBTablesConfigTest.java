package com.company.maintenance_reactive_api.infrastructure.configuration;


import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.domain.model.Maintenance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamoDBTablesConfigTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private DynamoDbAsyncTable<Machine> machineTable;

    @Mock
    private DynamoDbAsyncTable<Maintenance> maintenanceTable;

    @Test
    void machineTable_shouldCreateTable() {

        when(dynamoDbEnhancedAsyncClient.table(eq("Machines"), any(TableSchema.class)))
                .thenReturn(machineTable);

        DynamoDBTablesConfig config = new DynamoDBTablesConfig(dynamoDbEnhancedAsyncClient);

        DynamoDbAsyncTable<Machine> result = config.machineTable();

        assertNotNull(result);
        verify(dynamoDbEnhancedAsyncClient).table("Machines", TableSchema.fromBean(Machine.class));
    }

    @Test
    void maintenanceTable_shouldCreateTable() {

        when(dynamoDbEnhancedAsyncClient.table(eq("Maintenances"), any(TableSchema.class)))
                .thenReturn(maintenanceTable);

        DynamoDBTablesConfig config = new DynamoDBTablesConfig(dynamoDbEnhancedAsyncClient);

        DynamoDbAsyncTable<Maintenance> result = config.maintenanceTable();

        assertNotNull(result);
        verify(dynamoDbEnhancedAsyncClient).table("Maintenances", TableSchema.fromBean(Maintenance.class));
    }

    @Test
    void machineTable_shouldUseCorrectTableName() {

        when(dynamoDbEnhancedAsyncClient.table(eq("Machines"), any(TableSchema.class)))
                .thenReturn(machineTable);

        DynamoDBTablesConfig config = new DynamoDBTablesConfig(dynamoDbEnhancedAsyncClient);

        config.machineTable();

        verify(dynamoDbEnhancedAsyncClient).table("Machines", TableSchema.fromBean(Machine.class));
    }

    @Test
    void maintenanceTable_shouldUseCorrectTableName() {

        when(dynamoDbEnhancedAsyncClient.table(eq("Maintenances"), any(TableSchema.class)))
                .thenReturn(maintenanceTable);

        DynamoDBTablesConfig config = new DynamoDBTablesConfig(dynamoDbEnhancedAsyncClient);

        config.maintenanceTable();

        verify(dynamoDbEnhancedAsyncClient).table("Maintenances", TableSchema.fromBean(Maintenance.class));
    }
}
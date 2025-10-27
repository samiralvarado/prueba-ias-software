package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller;

import com.company.maintenance_reactive_api.application.mapper.MaintenanceMapper;
import com.company.maintenance_reactive_api.application.usecase.CreateMaintenanceUseCase;
import com.company.maintenance_reactive_api.application.usecase.FindMaintenanceByTypeUseCase;
import com.company.maintenance_reactive_api.domain.model.Maintenance;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.MaintenanceController;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MaintenanceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaintenanceControllerTest {

    @Mock
    private CreateMaintenanceUseCase createMaintenanceUseCase;

    @Mock
    private FindMaintenanceByTypeUseCase findMaintenanceByTypeUseCase;

    @Mock
    private MaintenanceMapper maintenanceMapper;

    @InjectMocks
    private MaintenanceController maintenanceController;

    @Test
    void createMaintenance_WhenValidDTO_ShouldReturnCreatedMaintenanceDTO() {

        MaintenanceDTO requestDTO = new MaintenanceDTO();
        requestDTO.setMachineId("machine-1");
        requestDTO.setType(Maintenance.MaintenanceType.PREVENTIVE);
        requestDTO.setDescription("Preventive maintenance");

        Maintenance domainMaintenance = new Maintenance("1", "machine-1",
                Maintenance.MaintenanceType.PREVENTIVE, "Preventive maintenance");
        MaintenanceDTO responseDTO = new MaintenanceDTO();
        responseDTO.setId("1");
        responseDTO.setMachineId("machine-1");
        responseDTO.setType(Maintenance.MaintenanceType.PREVENTIVE);
        responseDTO.setDescription("Preventive maintenance");

        when(maintenanceMapper.toDomain(requestDTO)).thenReturn(domainMaintenance);
        when(createMaintenanceUseCase.execute(domainMaintenance)).thenReturn(Mono.just(domainMaintenance));
        when(maintenanceMapper.toDTO(domainMaintenance)).thenReturn(responseDTO);


        StepVerifier.create(maintenanceController.createMaintenance(requestDTO))
                .expectNext(responseDTO)
                .verifyComplete();
    }

    @Test
    void getMaintenancesByType_WhenTypeExists_ShouldReturnMaintenanceDTOs() {
        // Given
        Maintenance.MaintenanceType type = Maintenance.MaintenanceType.PREVENTIVE;

        Maintenance maintenance1 = new Maintenance("1", "machine-1", type, "Preventive maintenance 1");
        Maintenance maintenance2 = new Maintenance("2", "machine-2", type, "Preventive maintenance 2");

        MaintenanceDTO dto1 = new MaintenanceDTO();
        dto1.setId("1");
        MaintenanceDTO dto2 = new MaintenanceDTO();
        dto2.setId("2");

        when(findMaintenanceByTypeUseCase.execute(type)).thenReturn(Flux.just(maintenance1, maintenance2));
        when(maintenanceMapper.toDTO(maintenance1)).thenReturn(dto1);
        when(maintenanceMapper.toDTO(maintenance2)).thenReturn(dto2);

        // When & Then
        StepVerifier.create(maintenanceController.getMaintenancesByType(type))
                .expectNext(dto1)
                .expectNext(dto2)
                .verifyComplete();
    }

    @Test
    void updateMaintenance_WhenValidRequest_ShouldReturnUpdatedMaintenanceDTO() {
        // Given
        String maintenanceId = "123";
        MaintenanceDTO requestDTO = new MaintenanceDTO();
        requestDTO.setMachineId("machine-1");
        requestDTO.setType(Maintenance.MaintenanceType.CORRECTIVE);
        requestDTO.setDescription("Corrective maintenance");

        Maintenance domainMaintenance = new Maintenance(maintenanceId, "machine-1",
                Maintenance.MaintenanceType.CORRECTIVE, "Corrective maintenance");
        MaintenanceDTO responseDTO = new MaintenanceDTO();
        responseDTO.setId(maintenanceId);
        responseDTO.setMachineId("machine-1");
        responseDTO.setType(Maintenance.MaintenanceType.CORRECTIVE);
        responseDTO.setDescription("Corrective maintenance");

        when(maintenanceMapper.toDomain(requestDTO)).thenReturn(domainMaintenance);
        when(createMaintenanceUseCase.execute(domainMaintenance)).thenReturn(Mono.just(domainMaintenance));
        when(maintenanceMapper.toDTO(domainMaintenance)).thenReturn(responseDTO);

        // When & Then
        StepVerifier.create(maintenanceController.updateMaintenance(maintenanceId, requestDTO))
                .expectNext(responseDTO)
                .verifyComplete();
    }

    @Test
    void updateMaintenance_ShouldSetIdFromPath() {
        // Given
        String maintenanceId = "123";
        MaintenanceDTO requestDTO = new MaintenanceDTO();
        requestDTO.setMachineId("machine-1");
        requestDTO.setType(Maintenance.MaintenanceType.CORRECTIVE);

        Maintenance domainMaintenance = new Maintenance(maintenanceId, "machine-1",
                Maintenance.MaintenanceType.CORRECTIVE, null);
        MaintenanceDTO responseDTO = new MaintenanceDTO();
        responseDTO.setId(maintenanceId);

        when(maintenanceMapper.toDomain(any(MaintenanceDTO.class))).thenReturn(domainMaintenance);
        when(createMaintenanceUseCase.execute(domainMaintenance)).thenReturn(Mono.just(domainMaintenance));
        when(maintenanceMapper.toDTO(domainMaintenance)).thenReturn(responseDTO);

        // When & Then
        StepVerifier.create(maintenanceController.updateMaintenance(maintenanceId, requestDTO))
                .expectNextMatches(dto -> maintenanceId.equals(dto.getId()))
                .verifyComplete();
    }
}

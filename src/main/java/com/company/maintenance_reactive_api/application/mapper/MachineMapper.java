package com.company.maintenance_reactive_api.application.mapper;

import com.company.maintenance_reactive_api.domain.model.Machine;
import com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto.MachineDTO;
import org.springframework.stereotype.Component;

@Component
public class MachineMapper {

    public MachineDTO toDTO(Machine machine) {
        if (machine == null) {
            return null;
        }

        MachineDTO dto = new MachineDTO();
        dto.setId(machine.getId());
        dto.setName(machine.getName());
        dto.setLocation(machine.getLocation());
        dto.setCreatedAt(machine.getCreatedAt());
        dto.setUpdatedAt(machine.getUpdatedAt());

        return dto;
    }

    public Machine toDomain(MachineDTO dto) {
        if (dto == null) {
            return null;
        }

        Machine machine = new Machine();
        machine.setId(dto.getId());
        machine.setName(dto.getName());
        machine.setLocation(dto.getLocation());

        return machine;
    }

    public Machine updateFromDTO(Machine existingMachine, MachineDTO dto) {
        if (existingMachine == null || dto == null) {
            return existingMachine;
        }

        if (dto.getName() != null) {
            existingMachine.setName(dto.getName());
        }
        if (dto.getLocation() != null) {
            existingMachine.setLocation(dto.getLocation());
        }

        existingMachine.updateTimestamp();

        return existingMachine;
    }
}
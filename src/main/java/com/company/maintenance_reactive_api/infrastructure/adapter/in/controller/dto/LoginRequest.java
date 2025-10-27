package com.company.maintenance_reactive_api.infrastructure.adapter.in.controller.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
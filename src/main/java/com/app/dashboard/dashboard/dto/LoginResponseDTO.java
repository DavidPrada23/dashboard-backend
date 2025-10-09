package com.app.dashboard.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private boolean claveTemporal;
    private boolean comercioActivo;
}

package com.app.dashboard.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private Long comercioId;
    private boolean comercioActivo;
}

package com.app.dashboard.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComercioResponseDTO {
    private Long id;
    private String nombre;
    private boolean activo;
    private String email;
    private String emailBancario;
    private String llaveActual;
}
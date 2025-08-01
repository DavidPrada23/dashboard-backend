package com.app.dashboard.dashboard.dto;

import lombok.Data;

//import jakarta.validation.constraints.*;

@Data
public class RegistroRequestDTO {

    //@NotBlank(message = "El correo no puede estar vacío")
    //@Email(message = "El correo no es válido")
    private String email;

    //@NotBlank(message = "La contraseña es obligatoria")
    //@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    //@NotNull(message = "El ID del comercio es obligatorio")
    private Long comercioId;

    //@NotBlank(message = "El rol es obligatorio")
    private String rol;
}

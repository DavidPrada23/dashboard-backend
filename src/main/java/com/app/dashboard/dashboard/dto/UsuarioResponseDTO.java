package com.app.dashboard.dashboard.dto;

import com.app.dashboard.dashboard.model.Usuario;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private boolean activo;
    private boolean debeCambiarPassword;
    private String comercioNombre;
    private boolean comercioActivo;
    private String comercioEmailBancario;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.activo = usuario.isActivo();
        this.debeCambiarPassword = usuario.isDebeCambiarPassword();
        if (usuario.getComercio() != null) {
            this.comercioNombre = usuario.getComercio().getNombre();
            this.comercioActivo = usuario.getComercio().isActivo();
            this.comercioEmailBancario = usuario.getComercio().getEmailBancario();
        }
    }
}

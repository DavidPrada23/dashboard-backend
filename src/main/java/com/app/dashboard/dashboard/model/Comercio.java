package com.app.dashboard.dashboard.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comercio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    @Column(nullable = false, name = "email_bancario")
    private String emailBancario;

    @Column(name = "llave_actual")
    private String llaveActual;

    @Column(name = "requiere_cambio_clave")
    private boolean requiereCambioClave = true;

    private boolean activo = false;

    @OneToMany(mappedBy = "comercio")
    private List<Usuario> usuarios;
}

package com.app.dashboard.dashboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class MedioPago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // "PSE", "TARJETA", "LLAVE_QR"

    private String descripcion;

    private String ultimoDigitos; // solo últimos 4 dígitos de la tarjeta

    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;
}

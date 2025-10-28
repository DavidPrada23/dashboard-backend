package com.app.dashboard.dashboard.dto;

import lombok.Data;

@Data
public class VentaRequestDTO {
    private String producto;
    private double monto;
    private String cliente;
    private String metodoPago;
}

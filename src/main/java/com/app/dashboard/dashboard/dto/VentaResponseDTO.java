package com.app.dashboard.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.app.dashboard.dashboard.model.Venta;

import lombok.Data;

@Data
public class VentaResponseDTO {
    private Long id;
    private String producto;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String comercioNombre;

    public VentaResponseDTO(Venta venta) {
        this.id = venta.getId();
        this.producto = venta.getProducto();
        this.monto = venta.getMonto();
        this.fecha = venta.getFecha();
        this.comercioNombre = venta.getComercio().getNombre();
    }
}

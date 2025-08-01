package com.app.dashboard.dashboard.controller;

import org.springframework.web.bind.annotation.RestController;

import com.app.dashboard.dashboard.dto.VentaRequestDTO;
import com.app.dashboard.dashboard.dto.VentaResponseDTO;
import com.app.dashboard.dashboard.exception.ComercioNoEncontradoException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.model.Venta;
import com.app.dashboard.dashboard.service.VentaService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@RequestBody VentaRequestDTO request) {
        Venta venta = ventaService.crearVenta(
            request.getProducto(),
            request.getMonto(),
            request.getEmailUsuario()
        );
        return ResponseEntity.ok(venta);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<VentaResponseDTO>> listarMisVentas(@AuthenticationPrincipal Usuario usuario) {
        Comercio comercio = usuario.getComercio();
        if (comercio == null) {
            throw new ComercioNoEncontradoException("El usuario no está vinculado a ningún comercio");
        }

        List<Venta> ventas = ventaService.listarVentasPorComercio(comercio.getId());
        List<VentaResponseDTO> response = ventas.stream()
            .map(VentaResponseDTO::new)
            .toList();

        return ResponseEntity.ok(response);
    }

}

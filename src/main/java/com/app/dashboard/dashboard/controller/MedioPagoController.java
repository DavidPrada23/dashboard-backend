package com.app.dashboard.dashboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dashboard.dashboard.model.MedioPago;
import com.app.dashboard.dashboard.service.MedioPagoService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/mediospago")
@RequiredArgsConstructor
public class MedioPagoController {
    
    private final MedioPagoService medioPagoService;

    @PostMapping
    public ResponseEntity<MedioPago> crear(@RequestBody MedioPago medioPago) {
        return ResponseEntity.ok(medioPagoService.guardar(medioPago));
    }

    @GetMapping("/{comercioId}")
    public ResponseEntity<List<MedioPago>> listar(@PathVariable Long comercioId) {
        return ResponseEntity.ok(medioPagoService.listarPorComercio(comercioId));
    }
    
}

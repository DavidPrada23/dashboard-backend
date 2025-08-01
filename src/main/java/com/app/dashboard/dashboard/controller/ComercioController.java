package com.app.dashboard.dashboard.controller;

import org.springframework.web.bind.annotation.RestController;

import com.app.dashboard.dashboard.dto.ComercioRegistroDTO;
import com.app.dashboard.dashboard.dto.LlaveDTO;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.service.ComercioService;
import com.app.dashboard.dashboard.service.QrService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/comercio")
@RequiredArgsConstructor
public class ComercioController {
    private final ComercioService comercioService;
    private final QrService qrService;

    @PutMapping("/llave")
    public ResponseEntity<String> actualizarLlave(@RequestBody LlaveDTO dto,
                                              @AuthenticationPrincipal(expression = "username") String correo) {
        comercioService.actualizarLlave(correo, dto.getNuevaLlave());
        return ResponseEntity.ok("Llave actualizada correctamente");
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerMiPerfil(@AuthenticationPrincipal(expression = "username") String correo) {
        return ResponseEntity.ok(comercioService.obtenerPerfil(correo));
    }

    @GetMapping("/qr")
    public ResponseEntity<byte[]> generarQr(@AuthenticationPrincipal(expression = "username") String correo) throws Exception {
        Comercio comercio = comercioService.obtenerPerfil(correo);
        byte[] qr = qrService.generarQR(comercio.getLlaveActual(), 250, 250);

        return ResponseEntity.ok()
            .header("Content-Type", "image/png")
            .body(qr);
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody ComercioRegistroDTO dto) {
        comercioService.registrarComercio(dto.getEmail(), dto.getNombre());
        return ResponseEntity.ok("Comercio registrado y correo enviado.");
    }


}

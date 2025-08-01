package com.app.dashboard.dashboard.controller;

import org.springframework.web.bind.annotation.RestController;

import com.app.dashboard.dashboard.dto.LlaveRequestDTO;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Llave;
import com.app.dashboard.dashboard.service.LlaveService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/llaves")
@RequiredArgsConstructor
public class LlaveController {
    private final LlaveService llaveService;
    private final LlaveWebSocketController llaveWebSocketController;

    @PostMapping
    public ResponseEntity<Llave> crearLlave(
            @RequestBody LlaveRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // obtienes el usuario logueado
        String emailUsuario = userDetails.getUsername();

        Llave guardada = llaveService.guardarLlaveDesdeEmail(
                emailUsuario,
                request.getValor(),
                request.getEmailBancario());

        // obtienes el comercio desde la llave
        Comercio comercio = guardada.getComercio();

        // Notificas por WebSocket
        llaveWebSocketController.notificarCambioLlave(
                guardada.getValor(),
                comercio.getId()
        // podrías notificar también el emailBancario si quieres
        );

        return ResponseEntity.ok(guardada);
    }

    @GetMapping("/ultima")
    public ResponseEntity<Llave> obtenerUltimaLlave(@AuthenticationPrincipal UserDetails userDetails) {
        return llaveService.obtenerUltimaLlaveDesdeEmail(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

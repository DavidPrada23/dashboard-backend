package com.app.dashboard.dashboard.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LlaveWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public void notificarCambioLlave(String nuevaLlave, Long comercioId) {
        String destino = "/topic/llave/" + comercioId;
        messagingTemplate.convertAndSend(destino, nuevaLlave);
    }
}

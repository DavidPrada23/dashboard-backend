package com.app.dashboard.dashboard.controller;

import org.springframework.web.bind.annotation.RestController;

import com.app.dashboard.dashboard.dto.ComercioRegistroDTO;
import com.app.dashboard.dashboard.dto.ComercioResponseDTO;
import com.app.dashboard.dashboard.dto.LlaveDTO;
import com.app.dashboard.dashboard.exception.UsuarioNoEncontradoException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.UsuarioRepository;
import com.app.dashboard.dashboard.service.ComercioService;
import com.app.dashboard.dashboard.service.QrService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/comercio")
@RequiredArgsConstructor
public class ComercioController {
    private final ComercioService comercioService;
    private final QrService qrService;
    private final UsuarioRepository usuarioRepository;

    @PutMapping("/llave")
    public ResponseEntity<String> actualizarLlave(@RequestBody LlaveDTO dto,
            @AuthenticationPrincipal(expression = "username") String correo) {
        comercioService.actualizarLlave(correo, dto.getValor());
        return ResponseEntity.ok("Llave actualizada correctamente");
    }

    @GetMapping("/me")
    public ResponseEntity<ComercioResponseDTO> obtenerMiComercio(@AuthenticationPrincipal UserDetails userDetails) {
        // 1️⃣ Busca el usuario autenticado
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // 2️⃣ Verifica que tenga un comercio vinculado
        Comercio comercio = usuario.getComercio();
        if (comercio == null) {
            throw new UsuarioNoEncontradoException("El usuario no está vinculado a ningún comercio");
        }

        // 3️⃣ Mapea a DTO de respuesta
        ComercioResponseDTO dto = new ComercioResponseDTO();
        dto.setId(comercio.getId());
        dto.setNombre(comercio.getNombre());
        dto.setActivo(comercio.isActivo());
        dto.setEmailBancario(comercio.getEmailBancario());
        dto.setLlaveActual(comercio.getLlaveActual());

        // 4️⃣ Devuelve respuesta
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/qr")
    public ResponseEntity<byte[]> generarQr(@AuthenticationPrincipal(expression = "username") String correo)
            throws Exception {
        Comercio comercio = comercioService.obtenerPerfil(correo);
        byte[] qr = qrService.generarQR(comercio.getLlaveActual(), 250, 250);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(qr);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody ComercioRegistroDTO request) {
        comercioService.registrarComercio(request.getEmail(), request.getNombre());
        return ResponseEntity.ok("Comercio registrado correctamente");
    }

}

package com.app.dashboard.dashboard.controller;

import com.app.dashboard.dashboard.dto.CambiarClaveDTO;
import com.app.dashboard.dashboard.dto.CompletarRegistroDTO;
import com.app.dashboard.dashboard.dto.LoginRequestDTO;
import com.app.dashboard.dashboard.dto.LoginResponseDTO;
import com.app.dashboard.dashboard.dto.RegistroRequestDTO;
import com.app.dashboard.dashboard.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> register(@RequestBody RegistroRequestDTO request) {
        authService.crearUsuario(request.getEmail(), request.getPassword(), false, request.getComercioId());
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/cambiar-clave")
    public ResponseEntity<String> cambiarClave(@RequestBody CambiarClaveDTO dto,
            @AuthenticationPrincipal UserDetails user) {
        authService.cambiarClave(user.getUsername(), dto.getNuevaClave());
        return ResponseEntity.ok("Clave actualizada");
    }

    @PostMapping("/completar-registro")
    public ResponseEntity<String> completarRegistro(@RequestBody CompletarRegistroDTO dto) {
        authService.completarRegistro(
                dto.getEmail(),
                dto.getNuevaClave(),
                dto.getCorreoBancario(),
                dto.getLlaveActual());
        return ResponseEntity.ok("Registro completado correctamente");
    }

}

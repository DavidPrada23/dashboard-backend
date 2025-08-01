package com.app.dashboard.dashboard.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.dashboard.dashboard.exception.ComercioNoEncontradoException;
import com.app.dashboard.dashboard.exception.UsuarioNoEncontradoException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Llave;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.ComercioRepository;
import com.app.dashboard.dashboard.repository.LlaveRepository;
import com.app.dashboard.dashboard.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LlaveService {
    private final LlaveRepository llaveRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioRepository comercioRepository;

    public Llave guardarLlaveDesdeEmail(String emailUsuario, String valor, String emailBancario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
    
        Comercio comercio = usuario.getComercio();
        if (comercio == null) {
            throw new ComercioNoEncontradoException("Este usuario no está asociado a ningún comercio");
        }
    
        if (emailBancario == null || emailBancario.isBlank()) {
            throw new IllegalArgumentException("El correo bancario es obligatorio para activar el comercio");
        }
    
        comercio.setEmailBancario(emailBancario);
        comercio.setActivo(true);
        comercioRepository.save(comercio);
    
        Llave nueva = new Llave();
        nueva.setValor(valor);
        nueva.setFechaGeneracion(LocalDateTime.now());
        nueva.setComercio(comercio);
    
        return llaveRepository.save(nueva);
    }
    
    

    public Optional<Llave> obtenerUltimaLlaveDesdeEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
    
        Comercio comercio = usuario.getComercio();
        if (comercio == null) {
            throw new UsuarioNoEncontradoException("El usuario no está vinculado a ningún comercio");
        }
    
        return llaveRepository.findTopByComercioIdOrderByFechaGeneracionDesc(comercio.getId());
    }
    

}

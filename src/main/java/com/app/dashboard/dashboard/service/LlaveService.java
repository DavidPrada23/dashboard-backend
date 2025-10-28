package com.app.dashboard.dashboard.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.dashboard.dashboard.dto.LlaveDTO;
import com.app.dashboard.dashboard.exception.UsuarioNoEncontradoException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Llave;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.ComercioRepository;
import com.app.dashboard.dashboard.repository.LlaveRepository;
import com.app.dashboard.dashboard.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LlaveService {
    private final LlaveRepository llaveRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComercioRepository comercioRepository;

    @Transactional
    public Llave guardarLlaveDesdeEmail(String emailUsuario, String valor, String emailBancario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Comercio comercio = usuario.getComercio();

        // Crear y guardar nueva llave
        Llave llave = new Llave();
        llave.setValor(valor);
        llave.setComercio(comercio);
        llaveRepository.save(llave);

        // ✅ Actualizar comercio con la nueva llave
        comercio.setLlaveActual(valor);
        if (emailBancario != null && !emailBancario.isBlank()) {
            comercio.setEmailBancario(emailBancario);
        }
        comercio.setActivo(true);
        comercioRepository.save(comercio);

        return llave;
    }

    public Optional<LlaveDTO> obtenerUltimaLlaveDesdeEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Comercio comercio = usuario.getComercio();
        if (comercio == null) {
            throw new RuntimeException("El usuario no está vinculado a ningún comercio");
        }

        return llaveRepository.findTopByComercioIdOrderByFechaGeneracionDesc(comercio.getId())
                .map(llave -> {
                    LlaveDTO dto = new LlaveDTO();
                    dto.setValor(llave.getValor()); 
                    return dto;
                });
    }

}

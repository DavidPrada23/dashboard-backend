package com.app.dashboard.dashboard.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dashboard.dashboard.exception.ComercioNoEncontradoException;
import com.app.dashboard.dashboard.exception.EmailYaRegistradoException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Rol;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.ComercioRepository;
import com.app.dashboard.dashboard.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

//import jakarta.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class ComercioService {
    private final ComercioRepository comercioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    // private final EmailService emailService;

    /**
     * Registrar comercio y usuario asociado.
     */
    @Transactional
    public void registrarComercio(String email, String nombre) {
        // Verifica si ya existe un usuario con ese correo
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailYaRegistradoException("El correo ya está registrado");
        }

        // Genera contraseña temporal
        String passwordTemporal = UUID.randomUUID().toString().substring(0, 8);

        // Crea el comercio asociado al usuario
        Comercio comercio = new Comercio();
        comercio.setEmail(email);
        comercio.setNombre(nombre);
        comercio.setLlaveActual(UUID.randomUUID().toString());
        comercio.setActivo(false); // Se activa después de completar registro
        comercioRepository.save(comercio);

        // Crea el usuario asociado al comercio
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(passwordTemporal));
        usuario.setDebeCambiarPassword(true);
        usuario.setRol(Rol.COMERCIO);
        usuario.setComercio(comercio);
        usuarioRepository.save(usuario);

        // Enviar correo con contraseña temporal
        /*
         * try {
         * emailService.enviarCorreoBienvenida(email, passwordTemporal);
         * } catch (MessagingException e) {
         * throw new RuntimeException("Error al enviar correo", e);
         * }
         */
    }

    /**
     * Obtener perfil del comercio por correo.
     */
    public Comercio obtenerPerfil(String email) {
        return comercioRepository.findByEmail(email)
                .orElseThrow(() -> new ComercioNoEncontradoException("Comercio no encontrado"));
    }

    /**
     * Actualizar la llave del comercio.
     */
    public void actualizarLlave(String email, String nuevaLlave) {
        Comercio comercio = comercioRepository.findByEmail(email)
                .orElseThrow(() -> new ComercioNoEncontradoException("Comercio no encontrado"));

        comercio.setLlaveActual(nuevaLlave);
        comercioRepository.save(comercio);
    }

}

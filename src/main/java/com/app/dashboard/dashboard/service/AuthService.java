package com.app.dashboard.dashboard.service;

import com.app.dashboard.dashboard.dto.LoginResponseDTO;
import com.app.dashboard.dashboard.exception.ComercioNoEncontradoException;
import com.app.dashboard.dashboard.exception.EmailYaRegistradoException;
import com.app.dashboard.dashboard.exception.UsuarioNoEncontradoException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.ComercioRepository;
import com.app.dashboard.dashboard.repository.UsuarioRepository;
import com.app.dashboard.dashboard.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ComercioRepository comercioRepository;

    public LoginResponseDTO login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtTokenProvider.generateToken(email);
        return new LoginResponseDTO(token, usuario.isClaveTemporal());
    }

    public Usuario crearUsuario(String email, String passwordPlano, boolean claveTemporal, Long comercioId) {
        Comercio comercio = comercioRepository.findById(comercioId)
                .orElseThrow(() -> new ComercioNoEncontradoException("Comercio no encontrado"));

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailYaRegistradoException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(passwordPlano));
        usuario.setClaveTemporal(claveTemporal);
        usuario.setDebeCambiarPassword(claveTemporal);
        usuario.setActivo(false); // Se activa después de completar registro
        usuario.setComercio(comercio);

        return usuarioRepository.save(usuario);
    }

    public void cambiarClave(String email, String nuevaClave) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevaClave));
        usuario.setClaveTemporal(false); // Ya no es temporal
        usuarioRepository.save(usuario);
    }

    public void completarRegistro(String email, String nuevaClave, String correoBancario, String llaveActual) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
    
        Comercio comercio = usuario.getComercio();
    
        usuario.setPassword(passwordEncoder.encode(nuevaClave));
        usuario.setClaveTemporal(false);
        usuario.setDebeCambiarPassword(false);
        usuario.setActivo(true);
    
        comercio.setEmailBancario(correoBancario);
        comercio.setLlaveActual(llaveActual);
        comercio.setActivo(true);
    
        usuarioRepository.save(usuario);
        comercioRepository.save(comercio);
    }
    

}

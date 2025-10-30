package com.app.dashboard.dashboard.service;

import com.app.dashboard.dashboard.dto.LoginResponseDTO;
import com.app.dashboard.dashboard.exception.ComercioNoEncontradoException;
import com.app.dashboard.dashboard.exception.EmailYaRegistradoException;
import com.app.dashboard.dashboard.exception.UsuarioNoEncontradoException;
import com.app.dashboard.dashboard.exception.UsuarioSinComercioException;
import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Rol;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.ComercioRepository;
import com.app.dashboard.dashboard.repository.UsuarioRepository;
import com.app.dashboard.dashboard.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
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

        String token = jwtTokenProvider.generateToken(usuario);
        boolean comercioActivo = usuario.getComercio() != null && usuario.getComercio().isActivo();
        return new LoginResponseDTO(token, usuario.isClaveTemporal(), comercioActivo);
    }

    public Usuario crearUsuario(String email, String passwordPlano, boolean claveTemporal, Long comercioId, String rol,
            Usuario usuarioActual) {
        if (usuarioActual.getComercio() == null) {
            throw new UsuarioSinComercioException("Usuario no tiene comercio asociado");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailYaRegistradoException("El correo ya está registrado");
        }

        Comercio comercio;
        if (usuarioActual.getRol() == Rol.SUPERADMIN) {
            // El superadmin puede elegir cualquier comercio
            comercio = comercioRepository.findById(comercioId)
                    .orElseThrow(() -> new ComercioNoEncontradoException("Comercio no encontrado"));
        } else {
            // El admin solo puede usar su propio comercio
            comercio = usuarioActual.getComercio();
            if (!comercio.getId().equals(comercioId)) {
                throw new RuntimeException("No puedes crear usuarios en otro comercio");
            }
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(passwordPlano));
        nuevoUsuario.setClaveTemporal(claveTemporal);
        nuevoUsuario.setDebeCambiarPassword(claveTemporal);
        nuevoUsuario.setActivo(false); // Se activa después de completar registro
        nuevoUsuario.setComercio(comercio);
        nuevoUsuario.setRol(Rol.valueOf(rol)); // o asigna un rol por defecto si es necesario

        return usuarioRepository.save(nuevoUsuario);
    }

    @Transactional
    public LoginResponseDTO cambiarClaveYGenerarToken(String email, String nuevaClave) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // Si ya no es temporal, puedes decidir si permites o no re-cambio
        usuario.setPassword(passwordEncoder.encode(nuevaClave));
        usuario.setClaveTemporal(false); // o setDebeCambiarPassword(false) según tu modelo
        usuarioRepository.save(usuario);

        String token = jwtTokenProvider.generateToken(usuario); // adapta a tu método
        boolean comercioActivo = usuario.getComercio() != null && usuario.getComercio().isActivo();

        return new LoginResponseDTO(token, false, comercioActivo);
    }

    @Transactional
    public LoginResponseDTO completarRegistroYGenerarToken(String email, String emailBancario, String llaveActual) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (usuario.getComercio() == null) {
            throw new UsuarioSinComercioException("Usuario no tiene comercio asociado");
        }

        Comercio comercio = comercioRepository.findById(usuario.getComercio().getId())
                .orElseThrow(() -> new ComercioNoEncontradoException("Comercio no encontrado"));

        comercio.setEmailBancario(emailBancario);
        comercio.setLlaveActual(llaveActual);
        comercio.setActivo(true);
        comercioRepository.save(comercio);

        // (opcional) actualizar usuario si guardas flags en usuario
        usuario.setComercio(comercio);
        usuarioRepository.save(usuario);

        String token = jwtTokenProvider.generateToken(usuario);
        boolean claveTemporal = usuario.isClaveTemporal(); // true/false segun tu entidad

        return new LoginResponseDTO(token, claveTemporal, true);
    }

}

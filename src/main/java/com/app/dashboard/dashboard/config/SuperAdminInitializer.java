package com.app.dashboard.dashboard.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${superadmin.email}")
    private String email;

    @Value("${superadmin.password}")
    private String password;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail(email)) {
            Usuario superadmin = new Usuario();
            superadmin.setEmail(email);
            superadmin.setPassword(passwordEncoder.encode(password));
            superadmin.setActivo(true);
            superadmin.setClaveTemporal(false);
            superadmin.setDebeCambiarPassword(false);
            superadmin.setRol("SUPERADMIN"); 
            
            usuarioRepository.save(superadmin);
            System.out.println("✅ Superadmin creado");
        } else {
            System.out.println("ℹ️ Superadmin ya existe");
        }
    }
}

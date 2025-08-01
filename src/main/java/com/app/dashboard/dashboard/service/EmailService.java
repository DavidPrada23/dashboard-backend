package com.app.dashboard.dashboard.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;

//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@RequiredArgsConstructor
public class EmailService {
    //private final JavaMailSender mailSender;

    /*public void enviarCorreoBienvenida(String destino, String passwordTemporal) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destino);
        helper.setSubject("Tu acceso a la pasarela de pagos");
        helper.setText("""
            <h3>Bienvenido a nuestra plataforma</h3>
            <p>Tu contraseña temporal es: <strong>%s</strong></p>
            <p>Por favor inicia sesión y cámbiala de inmediato.</p>
            """.formatted(passwordTemporal), true);

        mailSender.send(mensaje);
    }*/
}

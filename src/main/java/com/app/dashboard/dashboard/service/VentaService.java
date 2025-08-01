package com.app.dashboard.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.app.dashboard.dashboard.model.Comercio;
import com.app.dashboard.dashboard.model.Usuario;
import com.app.dashboard.dashboard.model.Venta;
import com.app.dashboard.dashboard.repository.UsuarioRepository;
import com.app.dashboard.dashboard.repository.VentaRepository;

import com.app.dashboard.dashboard.exception.UsuarioNoEncontradoException;
import com.app.dashboard.dashboard.exception.ComercioNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {
    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Venta crearVenta(String producto, double monto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con email " + emailUsuario + " no encontrado"));

        Comercio comercio = usuario.getComercio();
        if (comercio == null) {
            throw new ComercioNoEncontradoException("Este usuario no está asociado a un comercio");
        }
        
        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setMonto(BigDecimal.valueOf(monto));
        venta.setFecha(LocalDateTime.now());
        venta.setComercio(comercio);

        Venta guardada = ventaRepository.save(venta);

        // Notificar por WebSocket
        messagingTemplate.convertAndSend("/topic/ventas/" + emailUsuario, guardada);

        return guardada;
    }

    public List<Venta> listarVentasPorComercio(Long comercioId) {
        return ventaRepository.findByComercioIdOrderByFechaDesc(comercioId);
    }
    
}

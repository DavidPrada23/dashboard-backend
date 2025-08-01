package com.app.dashboard.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dashboard.dashboard.model.MedioPago;
import com.app.dashboard.dashboard.repository.MedioPagoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedioPagoService {

    private final MedioPagoRepository medioPagoRepository;

    public MedioPago guardar(MedioPago medioPago){
        return medioPagoRepository.save(medioPago);
    }

    public List<MedioPago> listarPorComercio(Long comercioId) {
        return medioPagoRepository.findByComercioId(comercioId);
    }
}

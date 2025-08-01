package com.app.dashboard.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.dashboard.dashboard.model.MedioPago;


public interface MedioPagoRepository extends JpaRepository<MedioPago, Long>{
    List<MedioPago> findByComercioId(Long comercioId);
}

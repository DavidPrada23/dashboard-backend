package com.app.dashboard.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.dashboard.dashboard.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{
    List<Venta> findByComercioIdOrderByFechaDesc(Long comercioId);
}
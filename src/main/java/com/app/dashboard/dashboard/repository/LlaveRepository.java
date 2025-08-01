package com.app.dashboard.dashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.dashboard.dashboard.model.Llave;

@Repository
public interface LlaveRepository extends JpaRepository<Llave, Long>{
    Optional<Llave> findTopByComercioIdOrderByFechaGeneracionDesc(Long comercioId);
}

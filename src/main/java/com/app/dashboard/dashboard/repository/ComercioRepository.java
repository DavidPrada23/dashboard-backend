package com.app.dashboard.dashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.dashboard.dashboard.model.Comercio;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long>{
    Optional<Comercio> findByEmail(String email);
}

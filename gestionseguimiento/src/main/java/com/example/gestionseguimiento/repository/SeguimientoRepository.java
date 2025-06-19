package com.example.gestionseguimiento.repository;

import com.example.gestionseguimiento.model.EstadoReparacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeguimientoRepository extends JpaRepository<EstadoReparacion, Long> {
    List<EstadoReparacion> findBySolicitudId(Long solicitudId);
}
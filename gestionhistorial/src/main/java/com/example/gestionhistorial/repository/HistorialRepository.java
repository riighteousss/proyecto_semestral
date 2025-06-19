package com.example.gestionhistorial.repository;

import com.example.gestionhistorial.model.HistorialReparacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialRepository extends JpaRepository<HistorialReparacion, Long> {
    // Método simple para buscar por solicitud
    List<HistorialReparacion> findBySolicitudId(Long solicitudId);
}


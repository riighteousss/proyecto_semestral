package com.example.gestionhistorial.repository;

import com.example.gestionhistorial.model.HistorialReparacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistorialRepository extends JpaRepository<HistorialReparacion, Long> {
    // Método simple para buscar por solicitud
    List<HistorialReparacion> findBySolicitudId(Long solicitudId);
    
    // Filtro por usuario
    List<HistorialReparacion> findByUsuario(String usuario);
    
    // Filtro por técnico - necesitarás agregar el campo tecnicoId a tu modelo
    List<HistorialReparacion> findByTecnicoId(Long tecnicoId);
    
    // Filtros combinados
    List<HistorialReparacion> findBySolicitudIdAndUsuario(Long solicitudId, String usuario);
    
    List<HistorialReparacion> findBySolicitudIdAndTecnicoId(Long solicitudId, Long tecnicoId);
    
    // Query personalizada para buscar por usuario con contenido específico en la acción
    @Query("SELECT h FROM HistorialReparacion h WHERE h.usuario = :usuario AND h.accion LIKE %:accion%")
    List<HistorialReparacion> findByUsuarioAndAccionContaining(@Param("usuario") String usuario, @Param("accion") String accion);
    
    // Query personalizada para buscar por técnico con contenido específico en la acción
    @Query("SELECT h FROM HistorialReparacion h WHERE h.tecnicoId = :tecnicoId AND h.accion LIKE %:accion%")
    List<HistorialReparacion> findByTecnicoIdAndAccionContaining(@Param("tecnicoId") Long tecnicoId, @Param("accion") String accion);
}

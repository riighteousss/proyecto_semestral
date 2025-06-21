package com.example.gestionasignaciones.repository;

import com.example.gestionasignaciones.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    
    // Buscar por técnico
    List<Asignacion> findByTecnicoId(Long tecnicoId);
    
    // Buscar por solicitud
    List<Asignacion> findBySolicitudId(Long solicitudId);
    
    // Buscar por estado
    List<Asignacion> findByEstado(String estado);
    
    // Buscar por técnico y estado
    List<Asignacion> findByTecnicoIdAndEstado(Long tecnicoId, String estado);
    
    // Consulta personalizada para buscar asignaciones por técnico y rango de fechas
    @Query("SELECT a FROM Asignacion a WHERE a.tecnicoId = :tecnicoId AND a.fechaAsignacion BETWEEN :fechaInicio AND :fechaFin")
    List<Asignacion> findByTecnicoIdAndFechasBetween(
        @Param("tecnicoId") Long tecnicoId, 
        @Param("fechaInicio") java.util.Date fechaInicio, 
        @Param("fechaFin") java.util.Date fechaFin
    );
}

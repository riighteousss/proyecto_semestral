package com.example.gestiontecnicos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.gestiontecnicos.model.tecnicos;

@Repository
public interface RepositoryTecnicos extends JpaRepository<tecnicos, Long>{
    
    // Buscar técnico por RUT
    Optional<tecnicos> findByRut(String rut);
    
    // Buscar técnicos por especialidad
    List<tecnicos> findByEspecialidad(String especialidad);
    
    // Buscar técnicos por nombre (búsqueda exacta)
    List<tecnicos> findByNombre(String nombre);
    
    // Buscar técnicos por especialidad ordenados por nombre
    List<tecnicos> findByEspecialidadOrderByNombre(String especialidad);
    
    // Buscar técnicos por nombre que contenga una cadena (búsqueda parcial)
    List<tecnicos> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe un técnico con un RUT específico
    boolean existsByRut(String rut);
    
    // Verificar si existe un técnico con un RUT específico excluyendo un ID
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM tecnicos t WHERE t.rut = :rut AND t.id != :id")
    boolean existsByRutAndIdNot(@Param("rut") String rut, @Param("id") Long id);
    
    // Contar técnicos por especialidad
    @Query("SELECT COUNT(t) FROM tecnicos t WHERE t.especialidad = :especialidad")
    long countByEspecialidad(@Param("especialidad") String especialidad);
    
    // Obtener todas las especialidades únicas
    @Query("SELECT DISTINCT t.especialidad FROM tecnicos t WHERE t.especialidad IS NOT NULL ORDER BY t.especialidad")
    List<String> findDistinctEspecialidades();
    
    // Buscar técnicos por especialidad ord
}
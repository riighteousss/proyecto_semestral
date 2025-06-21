package com.example.gestionsolicitudes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionsolicitudes.model.solicitud;

@Repository
public interface solicitudrepository extends JpaRepository<solicitud, Long>{
    
    // Método para encontrar solicitudes por ID de usuario
    List<solicitud> findByIdusuario(Long idusuario);
}
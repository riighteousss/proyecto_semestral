package com.example.gestionreportes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.gestionreportes.Model.reporte;

@Repository
public interface reporterepository extends JpaRepository<reporte, Long> {
    // Método para buscar reportes por usuario
    List<reporte> findByIdusuario(Long idusuario);
}
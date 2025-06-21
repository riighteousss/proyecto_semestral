package com.example.gestionreportes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionreportes.Model.reporte;
import java.util.List;


@Repository
public interface reporterepository extends JpaRepository<reporte, Long> {
 List<reporte> findByIdusuario(Long idusuario);
}

package com.example.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.equipos.model.equipo;
import java.util.List;


@Repository
public interface EquipoRepository extends JpaRepository<equipo , Long> {
    List<equipo> findByIdusuario(Long idUsuario);
}

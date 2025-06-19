package com.example.gestionasignaciones.repository;

import com.example.gestionasignaciones.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
}

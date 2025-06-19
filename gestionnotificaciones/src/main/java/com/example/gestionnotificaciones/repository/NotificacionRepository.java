package com.example.gestionnotificaciones.repository;

import com.example.gestionnotificaciones.model.Notificacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioId(Long usuarioId);
}

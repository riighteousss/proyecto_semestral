package com.example.gestionnotificaciones.service;

import com.example.gestionnotificaciones.model.Notificacion;
import com.example.gestionnotificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> buscarTodas() {
        return notificacionRepository.findAll();
    }

    public Notificacion crearNotificacion(Long usuarioId, String mensaje) {
        // Validación 1: ID de usuario requerido y positivo
        if (usuarioId == null || usuarioId <= 0) {
            throw new RuntimeException("El ID de usuario es inválido");
        }

        // Validación 2: Mensaje no vacío
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new RuntimeException("El mensaje no puede estar vacío");
        }

        // Validación 3: Longitud máxima del mensaje (500 caracteres)
        if (mensaje.length() > 500) {
            throw new RuntimeException("El mensaje no puede exceder 500 caracteres");
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(usuarioId);
        notificacion.setMensaje(mensaje);
        return notificacionRepository.save(notificacion);
    }

    // Nuevo método simple para buscar por usuario
    public List<Notificacion> buscarPorUsuario(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new RuntimeException("El ID de usuario es inválido");
        }
        return notificacionRepository.findByUsuarioId(usuarioId);
    }
}

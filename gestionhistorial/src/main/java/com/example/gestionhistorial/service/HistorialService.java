package com.example.gestionhistorial.service;

import com.example.gestionhistorial.model.HistorialReparacion;
import com.example.gestionhistorial.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistorialService {
    @Autowired
    private HistorialRepository historialRepository;

    public List<HistorialReparacion> buscarTodos() {
        return historialRepository.findAll();
    }

    public HistorialReparacion crearRegistro(Long solicitudId, String accion, String usuario) {
        // Validación 1: Campos obligatorios
        if (solicitudId == null) {
            throw new RuntimeException("El ID de solicitud es requerido");
        }
        
        if (accion == null || accion.trim().isEmpty()) {
            throw new RuntimeException("La acción no puede estar vacía");
        }
        
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new RuntimeException("El usuario es requerido");
        }

        // Validación 2: ID positivo
        if (solicitudId <= 0) {
            throw new RuntimeException("El ID de solicitud debe ser positivo");
        }

        // Validación 3: Longitud máxima para acción (100 caracteres)
        if (accion.length() > 100) {
            throw new RuntimeException("La acción no puede exceder 100 caracteres");
        }

        HistorialReparacion registro = new HistorialReparacion();
        registro.setSolicitudId(solicitudId);
        registro.setAccion(accion);
        registro.setUsuario(usuario);
        return historialRepository.save(registro);
    }

    // Método simple para buscar por solicitud
    public List<HistorialReparacion> buscarPorSolicitud(Long solicitudId) {
        if (solicitudId == null || solicitudId <= 0) {
            throw new RuntimeException("ID de solicitud inválido");
        }
        return historialRepository.findBySolicitudId(solicitudId);
    }
}
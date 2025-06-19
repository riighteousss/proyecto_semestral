package com.example.gestionseguimiento.service;

import com.example.gestionseguimiento.model.EstadoReparacion;
import com.example.gestionseguimiento.repository.SeguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SeguimientoService {
    @Autowired
    private SeguimientoRepository seguimientoRepository;

    // Estados permitidos
    private final List<String> ESTADOS_PERMITIDOS = Arrays.asList(
        "RECIBIDO", "EN_REVISION", "EN_REPARACION", "REPARADO", "ENTREGADO", "CANCELADO"
    );

    public List<EstadoReparacion> buscarTodos() {
        return seguimientoRepository.findAll();
    }

    public EstadoReparacion crearEstado(Long solicitudId, String estado, String observaciones) {
        // Validación 1: ID de solicitud válido
        if (solicitudId == null || solicitudId <= 0) {
            throw new RuntimeException("El ID de solicitud es inválido");
        }

        // Validación 2: Estado no nulo y permitido
        if (estado == null || !ESTADOS_PERMITIDOS.contains(estado)) {
            throw new RuntimeException("Estado no válido. Use: " + ESTADOS_PERMITIDOS);
        }

        // Validación 3: Longitud de observaciones (opcional)
        if (observaciones != null && observaciones.length() > 500) {
            throw new RuntimeException("Las observaciones no pueden exceder 500 caracteres");
        }

        EstadoReparacion estadoReparacion = new EstadoReparacion();
        estadoReparacion.setSolicitudId(solicitudId);
        estadoReparacion.setEstado(estado);
        estadoReparacion.setObservaciones(observaciones);
        return seguimientoRepository.save(estadoReparacion);
    }

    // Nuevo método para buscar por solicitud
    public List<EstadoReparacion> buscarPorSolicitud(Long solicitudId) {
        if (solicitudId == null || solicitudId <= 0) {
            throw new RuntimeException("El ID de solicitud es inválido");
        }
        return seguimientoRepository.findBySolicitudId(solicitudId);
    }
}
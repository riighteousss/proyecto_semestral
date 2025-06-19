package com.example.gestionasignaciones.service;

import com.example.gestionasignaciones.model.Asignacion;
import com.example.gestionasignaciones.repository.AsignacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class AsignacionService {
    @Autowired
    private AsignacionRepository asignacionRepository;

    // Estados permitidos (podrías mover esto a un enum)
    private final List<String> ESTADOS_PERMITIDOS = Arrays.asList(
        "PENDIENTE", "ASIGNADA", "EN_PROGRESO", "COMPLETADA", "CANCELADA"
    );

    public List<Asignacion> buscarTodas() {
        return asignacionRepository.findAll();
    }

    public Asignacion crearAsignacion(Long tecnicoId, Long solicitudId, String estado) {
        // Validación básica del estado al crear
        if (!ESTADOS_PERMITIDOS.contains(estado)) {
            throw new RuntimeException("Estado no válido. Use: " + ESTADOS_PERMITIDOS);
        }

        Asignacion asignacion = new Asignacion();
        asignacion.setTecnicoId(tecnicoId);
        asignacion.setSolicitudId(solicitudId);
        asignacion.setEstado(estado);
        return asignacionRepository.save(asignacion);
    }

    public Asignacion actualizarEstado(Long id, String nuevoEstado) {
        // Validación del estado
        if (!ESTADOS_PERMITIDOS.contains(nuevoEstado)) {
            throw new RuntimeException("Estado no válido. Use: " + ESTADOS_PERMITIDOS);
        }

        // Buscar y validar existencia
        Asignacion asignacion = asignacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        // Validar transición de estados (ejemplo básico)
        if (asignacion.getEstado().equals("COMPLETADA")) {
            throw new RuntimeException("No se puede modificar una asignación COMPLETADA");
        }

        // Actualizar estado
        asignacion.setEstado(nuevoEstado);
        return asignacionRepository.save(asignacion);
    }
}
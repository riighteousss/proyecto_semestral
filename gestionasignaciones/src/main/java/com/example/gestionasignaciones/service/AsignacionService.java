package com.example.gestionasignaciones.service;

import com.example.gestionasignaciones.model.Asignacion;
import com.example.gestionasignaciones.repository.AsignacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AsignacionService {
    @Autowired
    private AsignacionRepository asignacionRepository;

    // Estados permitidos (podrías mover esto a un enum)
    private final List<String> ESTADOS_PERMITIDOS = Arrays.asList(
        "PENDIENTE", "ASIGNADA", "EN_PROGRESO", "COMPLETADA", "CANCELADA"
    );

    // Buscar todas las asignaciones
    public List<Asignacion> buscarTodas() {
        return asignacionRepository.findAll();
    }

    // Buscar asignaciones por técnico
    public List<Asignacion> buscarPorTecnico(Long tecnicoId) {
        return asignacionRepository.findByTecnicoId(tecnicoId);
    }

    // Buscar asignaciones por solicitud
    public List<Asignacion> buscarPorSolicitud(Long solicitudId) {
        return asignacionRepository.findBySolicitudId(solicitudId);
    }

    // Buscar asignaciones por estado
    public List<Asignacion> buscarPorEstado(String estado) {
        if (!ESTADOS_PERMITIDOS.contains(estado)) {
            throw new RuntimeException("Estado no válido. Use: " + ESTADOS_PERMITIDOS);
        }
        return asignacionRepository.findByEstado(estado);
    }

    // Buscar una asignación por ID
    public Optional<Asignacion> buscarPorId(Long id) {
        return asignacionRepository.findById(id);
    }

    // Crear nueva asignación
    public Asignacion crearAsignacion(Long tecnicoId, Long solicitudId, String estado) {
        // Validación básica del estado al crear
        if (!ESTADOS_PERMITIDOS.contains(estado)) {
            throw new RuntimeException("Estado no válido. Use: " + ESTADOS_PERMITIDOS);
        }

        // Verificar que no exista ya una asignación para esta solicitud
        List<Asignacion> asignacionesExistentes = asignacionRepository.findBySolicitudId(solicitudId);
        if (!asignacionesExistentes.isEmpty()) {
            throw new RuntimeException("Ya existe una asignación para esta solicitud");
        }

        Asignacion asignacion = new Asignacion();
        asignacion.setTecnicoId(tecnicoId);
        asignacion.setSolicitudId(solicitudId);
        asignacion.setEstado(estado != null ? estado : "PENDIENTE");
        return asignacionRepository.save(asignacion);
    }

    // Actualizar estado de asignación
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

    // Modificar asignación completa
    public Asignacion modificarAsignacion(Long id, Long tecnicoId, Long solicitudId, String estado) {
        Asignacion asignacion = asignacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        // Validar estado si se proporciona
        if (estado != null && !ESTADOS_PERMITIDOS.contains(estado)) {
            throw new RuntimeException("Estado no válido. Use: " + ESTADOS_PERMITIDOS);
        }

        // Verificar que no sea una asignación completada
        if (asignacion.getEstado().equals("COMPLETADA")) {
            throw new RuntimeException("No se puede modificar una asignación COMPLETADA");
        }

        // Actualizar campos
        if (tecnicoId != null) asignacion.setTecnicoId(tecnicoId);
        if (solicitudId != null) asignacion.setSolicitudId(solicitudId);
        if (estado != null) asignacion.setEstado(estado);

        return asignacionRepository.save(asignacion);
    }

    // Eliminar asignación
    public void eliminarAsignacion(Long id) {
        Asignacion asignacion = asignacionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        // No permitir eliminar asignaciones completadas
        if (asignacion.getEstado().equals("COMPLETADA")) {
            throw new RuntimeException("No se puede eliminar una asignación COMPLETADA");
        }

        asignacionRepository.delete(asignacion);
    }
}
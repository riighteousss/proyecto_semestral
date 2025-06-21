package com.example.gestionasignaciones.controller;

import com.example.gestionasignaciones.model.Asignacion;
import com.example.gestionasignaciones.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AsignacionController {
    @Autowired
    private AsignacionService asignacionService;

    // Obtener todas las asignaciones
    @GetMapping("/asignaciones")
    public ResponseEntity<List<Asignacion>> obtenerAsignaciones() {
        List<Asignacion> asignaciones = asignacionService.buscarTodas();
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

    // Obtener asignación por ID
    @GetMapping("/asignaciones/{id}")
    public ResponseEntity<?> obtenerAsignacionPorId(@PathVariable Long id) {
        try {
            Optional<Asignacion> asignacion = asignacionService.buscarPorId(id);
            return asignacion.isPresent() ? 
                ResponseEntity.ok(asignacion.get()) : 
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Obtener asignaciones por técnico
    @GetMapping("/asignaciones/tecnico/{tecnicoId}")
    public ResponseEntity<List<Asignacion>> obtenerAsignacionesPorTecnico(@PathVariable Long tecnicoId) {
        List<Asignacion> asignaciones = asignacionService.buscarPorTecnico(tecnicoId);
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

    // Obtener asignaciones por solicitud
    @GetMapping("/asignaciones/solicitud/{solicitudId}")
    public ResponseEntity<List<Asignacion>> obtenerAsignacionesPorSolicitud(@PathVariable Long solicitudId) {
        List<Asignacion> asignaciones = asignacionService.buscarPorSolicitud(solicitudId);
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

    // Obtener asignaciones por estado
    @GetMapping("/asignaciones/estado/{estado}")
    public ResponseEntity<?> obtenerAsignacionesPorEstado(@PathVariable String estado) {
        try {
            List<Asignacion> asignaciones = asignacionService.buscarPorEstado(estado.toUpperCase());
            return asignaciones.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(asignaciones);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Crear nueva asignación
    @PostMapping("/asignaciones")
    public ResponseEntity<?> crearAsignacion(@RequestBody Asignacion asignacion) {
        try {
            Asignacion nuevaAsignacion = asignacionService.crearAsignacion(
                asignacion.getTecnicoId(),
                asignacion.getSolicitudId(),
                asignacion.getEstado()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAsignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Actualizar estado de asignación
    @PutMapping("/asignaciones/{id}/estado")
    public ResponseEntity<?> actualizarEstadoAsignacion(
            @PathVariable Long id,
            @RequestParam String estado) {
        try {
            Asignacion asignacionActualizada = asignacionService.actualizarEstado(id, estado.toUpperCase());
            return ResponseEntity.ok(asignacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Modificar asignación completa
    @PutMapping("/asignaciones/{id}")
    public ResponseEntity<?> modificarAsignacion(
            @PathVariable Long id,
            @RequestBody Asignacion asignacionData) {
        try {
            Asignacion asignacionActualizada = asignacionService.modificarAsignacion(
                id,
                asignacionData.getTecnicoId(),
                asignacionData.getSolicitudId(),
                asignacionData.getEstado()
            );
            return ResponseEntity.ok(asignacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Eliminar asignación
    @DeleteMapping("/asignaciones/{id}")
    public ResponseEntity<?> eliminarAsignacion(@PathVariable Long id) {
        try {
            asignacionService.eliminarAsignacion(id);
            return ResponseEntity.ok().body("Asignación eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
package com.example.gestionasignaciones.controller;

import com.example.gestionasignaciones.model.Asignacion;
import com.example.gestionasignaciones.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AsignacionController {
    @Autowired
    private AsignacionService asignacionService;

    @GetMapping("/asignaciones")
    public ResponseEntity<List<Asignacion>> obtenerAsignaciones() {
        List<Asignacion> asignaciones = asignacionService.buscarTodas();
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

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

    @PutMapping("/asignaciones/{id}/estado")
    public ResponseEntity<?> actualizarEstadoAsignacion(
            @PathVariable Long id,
            @RequestParam String estado) {
        try {
            Asignacion asignacionActualizada = asignacionService.actualizarEstado(id, estado);
            return ResponseEntity.ok(asignacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
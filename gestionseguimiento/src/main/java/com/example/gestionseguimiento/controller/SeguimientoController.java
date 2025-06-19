package com.example.gestionseguimiento.controller;

import com.example.gestionseguimiento.model.EstadoReparacion;
import com.example.gestionseguimiento.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SeguimientoController {
    @Autowired
    private SeguimientoService seguimientoService;

    @GetMapping("/estados-reparacion")
    public ResponseEntity<List<EstadoReparacion>> obtenerEstados() {
        List<EstadoReparacion> estados = seguimientoService.buscarTodos();
        return estados.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(estados);
    }

    @GetMapping("/estados-reparacion/solicitud/{solicitudId}")
    public ResponseEntity<?> obtenerEstadosPorSolicitud(@PathVariable Long solicitudId) {
        try {
            List<EstadoReparacion> estados = seguimientoService.buscarPorSolicitud(solicitudId);
            return estados.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(estados);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/estados-reparacion")
    public ResponseEntity<?> crearEstado(@RequestBody EstadoReparacion estado) {
        try {
            EstadoReparacion nuevoEstado = seguimientoService.crearEstado(
                estado.getSolicitudId(),
                estado.getEstado(),
                estado.getObservaciones()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
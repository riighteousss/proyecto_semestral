package com.example.gestionhistorial.controller;

import com.example.gestionhistorial.model.HistorialReparacion;
import com.example.gestionhistorial.service.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HistorialController {
    @Autowired
    private HistorialService historialService;

    @GetMapping("/historial")
    public ResponseEntity<List<HistorialReparacion>> obtenerHistorial() {
        List<HistorialReparacion> registros = historialService.buscarTodos();
        return registros.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(registros);
    }

    @GetMapping("/historial/solicitud/{solicitudId}")
    public ResponseEntity<?> obtenerPorSolicitud(@PathVariable Long solicitudId) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorSolicitud(solicitudId);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuevo endpoint: Filtrar por usuario
    @GetMapping("/historial/usuario/{usuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable String usuario) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorUsuario(usuario);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuevo endpoint: Filtrar por técnico
    @GetMapping("/historial/tecnico/{tecnicoId}")
    public ResponseEntity<?> obtenerPorTecnico(@PathVariable Long tecnicoId) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorTecnico(tecnicoId);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuevo endpoint: Filtrar por solicitud y usuario
    @GetMapping("/historial/solicitud/{solicitudId}/usuario/{usuario}")
    public ResponseEntity<?> obtenerPorSolicitudYUsuario(
            @PathVariable Long solicitudId, 
            @PathVariable String usuario) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorSolicitudYUsuario(solicitudId, usuario);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuevo endpoint: Filtrar por solicitud y técnico
    @GetMapping("/historial/solicitud/{solicitudId}/tecnico/{tecnicoId}")
    public ResponseEntity<?> obtenerPorSolicitudYTecnico(
            @PathVariable Long solicitudId, 
            @PathVariable Long tecnicoId) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorSolicitudYTecnico(solicitudId, tecnicoId);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuevo endpoint: Filtrar por usuario con parámetro de acción
    @GetMapping("/historial/usuario/{usuario}/accion")
    public ResponseEntity<?> obtenerPorUsuarioYAccion(
            @PathVariable String usuario,
            @RequestParam String filtro) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorUsuarioYAccion(usuario, filtro);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nuevo endpoint: Filtrar por técnico con parámetro de acción
    @GetMapping("/historial/tecnico/{tecnicoId}/accion")
    public ResponseEntity<?> obtenerPorTecnicoYAccion(
            @PathVariable Long tecnicoId,
            @RequestParam String filtro) {
        try {
            List<HistorialReparacion> registros = historialService.buscarPorTecnicoYAccion(tecnicoId, filtro);
            return registros.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(registros);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/historial")
    public ResponseEntity<?> crearRegistro(@RequestBody HistorialReparacion registro) {
        try {
            HistorialReparacion nuevoRegistro = historialService.crearRegistro(
                registro.getSolicitudId(),
                registro.getAccion(),
                registro.getUsuario(),
                registro.getTecnicoId()  // Ahora incluye el técnico
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


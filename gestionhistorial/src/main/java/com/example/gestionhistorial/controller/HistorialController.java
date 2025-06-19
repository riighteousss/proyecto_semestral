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

    @PostMapping("/historial")
    public ResponseEntity<?> crearRegistro(@RequestBody HistorialReparacion registro) {
        try {
            HistorialReparacion nuevoRegistro = historialService.crearRegistro(
                registro.getSolicitudId(),
                registro.getAccion(),
                registro.getUsuario()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
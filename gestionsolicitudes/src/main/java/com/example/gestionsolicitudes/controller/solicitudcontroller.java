package com.example.gestionsolicitudes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionsolicitudes.model.solicitud;
import com.example.gestionsolicitudes.services.solicitudservice;

@RestController
@RequestMapping("api/v1")
public class solicitudcontroller {
    @Autowired
    private solicitudservice solicitudservice;

    // Endpoint para consultar todas las solicitudes
    @GetMapping("/solicitudes")
    public ResponseEntity<List<solicitud>> obtenerSolicitudes(){
        List<solicitud> solicitudes = solicitudservice.buscarsolicitudes();
        return solicitudes.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(solicitudes);
    }

    // Endpoint para visualizar solicitudes por usuario específico
    @GetMapping("/solicitudes/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerSolicitudesPorUsuario(@PathVariable Long idUsuario) {
        try {
            List<solicitud> solicitudes = solicitudservice.buscarSolicitudesPorUsuario(idUsuario);
            return solicitudes.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(solicitudes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para visualizar una solicitud específica por ID
    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<?> obtenerSolicitudPorId(@PathVariable Long id) {
        try {
            solicitud solicitud = solicitudservice.buscarSolicitudPorId(id);
            return ResponseEntity.ok(solicitud);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para crear una nueva solicitud
    @PostMapping("/solicitudes")
    public ResponseEntity<?> crearSolicitud(@RequestBody solicitud solicitud){
        try {
            solicitud nuevaSolicitud = solicitudservice.crearsolicitud(
                solicitud.getTiposolicitud(),
                solicitud.getDescripciongeneral(),
                solicitud.getIdusuario()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para modificar una solicitud existente
    @PutMapping("/solicitudes/{id}")
    public ResponseEntity<?> modificarSolicitud(
            @PathVariable Long id,
            @RequestParam(required = false) String tiposolicitud,
            @RequestParam(required = false) String descripciongeneral) {
        try {
            solicitud solicitudActualizada = solicitudservice.modificarSolicitud(
                id, tiposolicitud, descripciongeneral);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint alternativo para modificar usando RequestBody
    @PutMapping("/solicitudes/{id}/completa")
    public ResponseEntity<?> modificarSolicitudCompleta(
            @PathVariable Long id,
            @RequestBody solicitud solicitudActualizada) {
        try {
            solicitud solicitud = solicitudservice.modificarSolicitud(
                id, 
                solicitudActualizada.getTiposolicitud(), 
                solicitudActualizada.getDescripciongeneral()
            );
            return ResponseEntity.ok(solicitud);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para eliminar una solicitud
    @DeleteMapping("/solicitudes/{id}")
    public ResponseEntity<?> eliminarSolicitud(@PathVariable Long id) {
        try {
            solicitudservice.eliminarSolicitud(id);
            return ResponseEntity.ok("Solicitud eliminada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
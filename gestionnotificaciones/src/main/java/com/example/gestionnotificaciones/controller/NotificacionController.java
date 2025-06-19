package com.example.gestionnotificaciones.controller;

import com.example.gestionnotificaciones.model.Notificacion;
import com.example.gestionnotificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/notificaciones")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.buscarTodas();
        return notificaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/notificaciones/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Notificacion> notificaciones = notificacionService.buscarPorUsuario(usuarioId);
            return notificaciones.isEmpty() ? 
                ResponseEntity.noContent().build() : 
                ResponseEntity.ok(notificaciones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/notificaciones")
    public ResponseEntity<?> crearNotificacion(@RequestBody Notificacion notificacion) {
        try {
            Notificacion nuevaNotificacion = notificacionService.crearNotificacion(
                notificacion.getUsuarioId(),
                notificacion.getMensaje()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

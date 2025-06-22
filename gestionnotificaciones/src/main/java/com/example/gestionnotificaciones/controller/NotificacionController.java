package com.example.gestionnotificaciones.controller;

import com.example.gestionnotificaciones.model.Notificacion;
import com.example.gestionnotificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "notificaciones",description = "operaciones relacionadas con la gestión de notificaciones")
@RestController
@RequestMapping("/api/v1")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

      //endpoint para obtener todas las notificaciones
     @Operation(summary = "obtener todas las notificaciones", description = "devuelve una lista con todas las notificaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay notificaciones registradas")
    })
    @GetMapping("/notificaciones")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.buscarTodas();
        return notificaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(notificaciones);
    }
    //endpoint para obtener todas las notificaciones de un usuario
     @Operation(summary = "obtener todas las notificaciones de un usuario", description = "devuelve una lista con todas las notificaciones de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Notificacion.class))),
             @ApiResponse(responseCode = "204", description = "El usuario no tiene notificaciones"),
        @ApiResponse(responseCode = "400", description = "ID de usuario inválido o error en la solicitud")
    })
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
    //endpoint para crear una notificacion 
    @Operation(summary = "Crear una nueva notificación", description = "Permite crear una nueva notificación para un usuario específico.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Notificación creada correctamente",
        content = @Content(schema = @Schema(implementation = Notificacion.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. datos inválidos.")
})
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

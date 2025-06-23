package com.example.gestionasignaciones.controller;

import com.example.gestionasignaciones.model.Asignacion;
import com.example.gestionasignaciones.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Asignaciones", description = "Operaciones relacionadas con la gestión de asignaciones de trabajo")
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AsignacionController {
    
    @Autowired
    private AsignacionService asignacionService;

    @Operation(summary = "Obtener todas las asignaciones", 
               description = "Devuelve una lista con todas las asignaciones existentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de asignaciones obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay asignaciones registradas")
    })
    @GetMapping("/asignaciones")
    public ResponseEntity<List<Asignacion>> obtenerAsignaciones() {
        List<Asignacion> asignaciones = asignacionService.buscarTodas();
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

    @Operation(summary = "Obtener asignación por ID", 
               description = "Devuelve los datos de la asignación solicitada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asignación encontrada correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "404", description = "Asignación no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Obtener asignaciones por técnico", 
               description = "Devuelve una lista de asignaciones asociadas a un técnico específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de asignaciones obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay asignaciones para el técnico especificado")
    })
    @GetMapping("/asignaciones/tecnico/{tecnicoId}")
    public ResponseEntity<List<Asignacion>> obtenerAsignacionesPorTecnico(@PathVariable Long tecnicoId) {
        List<Asignacion> asignaciones = asignacionService.buscarPorTecnico(tecnicoId);
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

    @Operation(summary = "Obtener asignaciones por solicitud", 
               description = "Devuelve una lista de asignaciones asociadas a una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de asignaciones obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay asignaciones para la solicitud especificada")
    })
    @GetMapping("/asignaciones/solicitud/{solicitudId}")
    public ResponseEntity<List<Asignacion>> obtenerAsignacionesPorSolicitud(@PathVariable Long solicitudId) {
        List<Asignacion> asignaciones = asignacionService.buscarPorSolicitud(solicitudId);
        return asignaciones.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(asignaciones);
    }

    @Operation(summary = "Obtener asignaciones por estado", 
               description = "Devuelve una lista de asignaciones filtradas por estado (Pendiente, En progreso, Completada, Cancelada)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de asignaciones obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay asignaciones con el estado especificado"),
        @ApiResponse(responseCode = "400", description = "Estado no válido")
    })
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

    @Operation(summary = "Crear nueva asignación", 
               description = "Registra una nueva asignación de trabajo a un técnico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Asignación creada correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la asignación no válidos")
    })
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

    @Operation(summary = "Actualizar estado de asignación", 
               description = "Permite actualizar el estado de una asignación existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de asignación actualizado correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "400", description = "Estado no válido o asignación no encontrada")
    })
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

    @Operation(summary = "Modificar asignación completa", 
               description = "Permite actualizar todos los datos de una asignación existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asignación actualizada correctamente",
            content = @Content(schema = @Schema(implementation = Asignacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos no válidos o asignación no encontrada")
    })
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

    @Operation(summary = "Eliminar asignación", 
               description = "Elimina una asignación existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asignación eliminada correctamente"),
        @ApiResponse(responseCode = "400", description = "Asignación no encontrada o no se puede eliminar")
    })
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
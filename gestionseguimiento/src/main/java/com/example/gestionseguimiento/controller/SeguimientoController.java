package com.example.gestionseguimiento.controller;

import com.example.gestionseguimiento.model.EstadoReparacion;
import com.example.gestionseguimiento.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "seguimiento", description = "Operaciones relacionadas con el seguimiento y estados de las reparaciones")
@RestController
@RequestMapping("/api/v1")
public class SeguimientoController {
    @Autowired
    private SeguimientoService seguimientoService;

    @Operation(summary = "Obtener todos los estados de reparación", description = "Devuelve una lista con todos los estados de reparación registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estados obtenida correctamente",
            content = @Content(schema = @Schema(implementation = EstadoReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay estados de reparación registrados")
    })
    @GetMapping("/estados-reparacion")
    public ResponseEntity<List<EstadoReparacion>> obtenerEstados() {
        List<EstadoReparacion> estados = seguimientoService.buscarTodos();
        return estados.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(estados);
    }

    @Operation(summary = "Obtener estados por solicitud", description = "Devuelve todos los estados asociados a una solicitud de reparación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estados de la solicitud encontrados correctamente",
            content = @Content(schema = @Schema(implementation = EstadoReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay estados registrados para la solicitud especificada"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o parámetros inválidos")
    })
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

    @Operation(summary = "Crear nuevo estado de reparación", description = "Permite registrar un nuevo estado para el seguimiento de una reparación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estado creado correctamente",
            content = @Content(schema = @Schema(implementation = EstadoReparacion.class))),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados, no se puede crear el estado")
    })
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
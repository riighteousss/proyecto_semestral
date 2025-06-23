package com.example.gestionhistorial.controller;

import com.example.gestionhistorial.model.HistorialReparacion;
import com.example.gestionhistorial.service.HistorialService;
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

@Tag(name = "historial", description = "Operaciones relacionadas con la gestión del historial de reparaciones")
@RestController
@RequestMapping("/api/v1")
public class HistorialController {
    @Autowired
    private HistorialService historialService;

    @Operation(summary = "Obtener todo el historial", description = "Devuelve una lista con todos los registros del historial de reparaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de registros obtenida correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros en el historial")
    })
    @GetMapping("/historial")
    public ResponseEntity<List<HistorialReparacion>> obtenerHistorial() {
        List<HistorialReparacion> registros = historialService.buscarTodos();
        return registros.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(registros);
    }

    @Operation(summary = "Obtener historial por solicitud", description = "Devuelve todos los registros asociados a una solicitud específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para la solicitud especificada"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o parámetros inválidos")
    })
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

    @Operation(summary = "Obtener historial por usuario", description = "Devuelve todos los registros asociados a un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros del usuario encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para el usuario especificado"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o usuario inválido")
    })
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

    @Operation(summary = "Obtener historial por técnico", description = "Devuelve todos los registros asociados a un técnico específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros del técnico encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para el técnico especificado"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o técnico inválido")
    })
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

    @Operation(summary = "Obtener historial por solicitud y usuario", description = "Devuelve los registros filtrados por solicitud y usuario específicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para los criterios especificados"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o parámetros inválidos")
    })
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

    @Operation(summary = "Obtener historial por solicitud y técnico", description = "Devuelve los registros filtrados por solicitud y técnico específicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para los criterios especificados"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o parámetros inválidos")
    })
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

    @Operation(summary = "Obtener historial por usuario y acción", description = "Devuelve los registros de un usuario filtrados por tipo de acción")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros filtrados encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para los criterios especificados"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o parámetros inválidos")
    })
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

    @Operation(summary = "Obtener historial por técnico y acción", description = "Devuelve los registros de un técnico filtrados por tipo de acción")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros filtrados encontrados correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros para los criterios especificados"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o parámetros inválidos")
    })
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

    @Operation(summary = "Crear nuevo registro en el historial", description = "Permite agregar un nuevo registro al historial de reparaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registro creado correctamente",
            content = @Content(schema = @Schema(implementation = HistorialReparacion.class))),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados, no se puede crear el registro")
    })
    @PostMapping("/historial")
    public ResponseEntity<?> crearRegistro(@RequestBody HistorialReparacion registro) {
        try {
            HistorialReparacion nuevoRegistro = historialService.crearRegistro(
                registro.getSolicitudId(),
                registro.getAccion(),
                registro.getUsuario(),
                registro.getTecnicoId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

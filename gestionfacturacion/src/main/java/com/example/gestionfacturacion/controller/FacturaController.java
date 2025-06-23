package com.example.gestionfacturacion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.gestionfacturacion.model.Factura;
import com.example.gestionfacturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
    name = "Facturas API",
    description = "API para gestión completa de facturas con validaciones de estados de pago"
)
@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Operation(
        summary = "Obtener todas las facturas",
        description = "Retorna lista paginada de facturas con filtros opcionales por estado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Facturas encontradas",
            content = @Content(schema = @Schema(implementation = Factura.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay facturas registradas",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<Factura>> listarFacturas() {
        List<Factura> facturas = facturaService.buscarTodas();
        return facturas.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(facturas);
    }

    @Operation(
        summary = "Buscar factura por ID",
        description = "Obtiene los detalles completos de una factura específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Factura encontrada",
            content = @Content(schema = @Schema(implementation = Factura.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Factura no encontrada",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerFactura(
            @Parameter(description = "ID de la factura", required = true)
            @PathVariable Long id) {
        return facturaService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear factura",
        description = "Registra una nueva factura con validación automática de estados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Factura creada exitosamente",
            content = @Content(schema = @Schema(implementation = Factura.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o estado no permitido",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<?> crearFactura(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la factura",
                required = true,
                content = @Content(schema = @Schema(implementation = Factura.class))
            )
            @RequestBody Factura factura) {
        try {
            Factura nuevaFactura = facturaService.crearFactura(
                factura.getSolicitudId(),
                factura.getMonto(),
                factura.getEstadoPago()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Actualizar estado de pago",
        description = "Modifica el estado de pago con validación de transiciones permitidas"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estado actualizado",
            content = @Content(schema = @Schema(implementation = Factura.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado inválido o transición no permitida",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Factura no encontrada",
            content = @Content
        )
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @Parameter(description = "ID de la factura") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevo estado de pago",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        type = "object",
                        example = "{\"estadoPago\": \"PAGADA\"}"
                    )
                )
            )
            @RequestBody Map<String, String> estado) {
        try {
            Factura actualizada = facturaService.actualizarEstadoPago(
                id, 
                estado.get("estadoPago")
            );
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return e.getMessage().contains("no encontrada")
                ? ResponseEntity.notFound().build()
                : ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.example.gestionfacturacion.controller;

import com.example.gestionfacturacion.model.Factura;
import com.example.gestionfacturacion.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FacturaController {
    @Autowired
    private FacturaService facturaService;

    @GetMapping("/facturas")
    public ResponseEntity<List<Factura>> obtenerFacturas() {
        List<Factura> facturas = facturaService.buscarTodas();
        return facturas.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(facturas);
    }

    @PostMapping("/facturas")
    public ResponseEntity<?> crearFactura(@RequestBody Factura factura) {
        try {
            Factura nuevaFactura = facturaService.crearFactura(
                factura.getSolicitudId(),
                factura.getMonto(),
                factura.getEstadoPago()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/facturas/{id}/estado-pago")
    public ResponseEntity<?> actualizarEstadoPago(
            @PathVariable Long id,
            @RequestParam String estadoPago) {
        try {
            Factura facturaActualizada = facturaService.actualizarEstadoPago(id, estadoPago);
            return ResponseEntity.ok(facturaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
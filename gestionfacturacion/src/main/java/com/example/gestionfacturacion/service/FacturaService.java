package com.example.gestionfacturacion.service;

import com.example.gestionfacturacion.model.Factura;
import com.example.gestionfacturacion.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;

    // Estados de pago permitidos
    private final List<String> ESTADOS_PAGO_PERMITIDOS = Arrays.asList(
        "PENDIENTE", "PARCIAL", "PAGADA", "VENCIDA", "CANCELADA"
    );

    public List<Factura> buscarTodas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> buscarPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public Factura crearFactura(Long solicitudId, BigDecimal monto, String estadoPago) {
        // Validación de monto positivo
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor que cero");
        }

        // Validación de estado de pago
        if (!ESTADOS_PAGO_PERMITIDOS.contains(estadoPago)) {
            throw new RuntimeException("Estado de pago no válido. Use: " + ESTADOS_PAGO_PERMITIDOS);
        }

        Factura factura = new Factura();
        factura.setSolicitudId(solicitudId);
        factura.setMonto(monto);
        factura.setEstadoPago(estadoPago);
        return facturaRepository.save(factura);
    }

    public Factura actualizarEstadoPago(Long id, String nuevoEstadoPago) {
        // Validación de estado
        if (!ESTADOS_PAGO_PERMITIDOS.contains(nuevoEstadoPago)) {
            throw new RuntimeException("Estado de pago no válido. Use: " + ESTADOS_PAGO_PERMITIDOS);
        }

        // Buscar y validar existencia
        Factura factura = facturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));

        // Validar transición de estados (ejemplo: no se puede cambiar de CANCELADA)
        if (factura.getEstadoPago().equals("CANCELADA")) {
            throw new RuntimeException("No se puede modificar una factura CANCELADA");
        }

        // Actualizar estado
        factura.setEstadoPago(nuevoEstadoPago);
        return facturaRepository.save(factura);
    }
}
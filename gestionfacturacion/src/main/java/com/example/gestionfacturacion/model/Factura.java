package com.example.gestionfacturacion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Facturas")
@Entity
@Schema(description = "Modelo que representa una factura en el sistema")
public class Factura {
    @Schema(description = "ID autoincrementable de la factura")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Schema(description = "ID de la solicitud asociada a esta factura", required = true)
    @Column(nullable = false)
    private Long solicitudId; // FK de solicitud
    
    @Schema(description = "Monto total de la factura", example = "125000.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;
    
    @Schema(description = "Fecha de emisión de la factura (generada automáticamente)")
    @Column(nullable = false)
    private Date fechaEmision;
    
    @Schema(
        description = "Estado de pago de la factura", 
        example = "PENDIENTE/PAGADA/ANULADA",
        allowableValues = {"PENDIENTE", "PAGADA", "ANULADA"}
    )
    @Column(nullable = false, length = 20)
    private String estadoPago;
    
    @PrePersist
    protected void onCreate() {
        this.fechaEmision = new Date();
        if (this.estadoPago == null) {
            this.estadoPago = "PENDIENTE";
        }
    }
}
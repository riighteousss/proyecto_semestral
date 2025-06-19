package com.example.gestionfacturacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long solicitudId; //FK de solicitud
    private BigDecimal monto;
    private Date fechaEmision = new Date();
    private String estadoPago;
    
    @PrePersist
    protected void onCreate() {
        this.fechaEmision = new Date();
    }
}//hola
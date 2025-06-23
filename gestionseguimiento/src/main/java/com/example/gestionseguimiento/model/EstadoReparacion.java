package com.example.gestionseguimiento.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Estados")
@Schema(description = "Estado de reparación - Registro del seguimiento de estados de las solicitudes de reparación")
public class EstadoReparacion {
    
    @Schema(description = "ID autoincrementable del estado de reparación")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Schema(description = "ID de la solicitud de reparación asociada", required = true, example = "1")
    @Column(nullable = false)
    private Long solicitudId;
    
    @Schema(description = "Estado actual de la reparación", example = "Pendiente, En proceso, Completado, Cancelado, Entregado")
    private String estado;
    
    @Schema(description = "Fecha y hora de la última actualización del estado", example = "2024-01-15T14:30:00")
    private Date fechaActualizacion = new Date();
    
    @Schema(description = "Observaciones adicionales sobre el cambio de estado", example = "Esperando aprobación del cliente, Falta repuesto, etc.")
    private String observaciones;
    
    @PrePersist
    protected void onCreate() {
        this.fechaActualizacion = new Date();
    }
}
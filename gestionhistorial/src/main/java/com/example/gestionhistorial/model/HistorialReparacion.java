package com.example.gestionhistorial.model;

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
@Table(name = "Historiales")
@Schema(description = "Historial de reparaciones - Registro de todas las acciones realizadas en las solicitudes de reparación")
public class HistorialReparacion {
    
    @Schema(description = "ID autoincrementable del registro de historial")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Schema(description = "ID de la solicitud de reparación asociada", required = true, example = "1")
    @Column(nullable = false)
    private Long solicitudId;
    
    @Schema(description = "Descripción de la acción realizada", example = "Solicitud creada, Técnico asignado, Reparación completada, etc.")
    private String accion;
    
    @Schema(description = "Fecha y hora en que se registró la acción", example = "2024-01-15T10:30:00")
    private Date fechaRegistro = new Date();
    
    @Schema(description = "Usuario que realizó la acción", example = "admin, juan.perez, sistema")
    private String usuario;
    
    @Schema(description = "ID del técnico asociado a la acción (opcional)", example = "5")
    private Long tecnicoId;
    
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = new Date();
    }
}
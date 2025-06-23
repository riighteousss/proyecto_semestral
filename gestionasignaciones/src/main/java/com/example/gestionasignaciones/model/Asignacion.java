package com.example.gestionasignaciones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "asignaciones")
@Schema(description = "Modelo que representa una asignación de trabajo a un técnico")
public class Asignacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincrementable de la asignación", example = "1")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "ID del técnico asignado (clave foránea)", example = "5", required = true)
    private Long tecnicoId;
    
    @Column(nullable = false)
    @Schema(description = "ID de la solicitud asignada (clave foránea)", example = "10", required = true)
    private Long solicitudId;
    
    @Schema(description = "Fecha de creación de la asignación. Se genera automáticamente al crear", 
           example = "2023-05-15T10:30:00.000Z")
    private Date fechaAsignacion = new Date();
    
    @Schema(description = "Estado actual de la asignación", 
           example = "Pendiente|En progreso|Completada|Cancelada")
    private String estado;
    
    @PrePersist
    protected void onCreate() {
        this.fechaAsignacion = new Date();
    }
}
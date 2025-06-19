package com.example.gestionseguimiento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "Estados")
public class EstadoReparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long solicitudId;
    
    private String estado;
    private Date fechaActualizacion = new Date();
    private String observaciones;
    
    @PrePersist
    protected void onCreate() {
        this.fechaActualizacion = new Date();
    }
}

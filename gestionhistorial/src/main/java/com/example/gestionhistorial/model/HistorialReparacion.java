package com.example.gestionhistorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name ="Historiales")
public class HistorialReparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long solicitudId;
    
    private String accion;
    private Date fechaRegistro = new Date();
    private String usuario;
    
    // Nuevo campo para el técnico asignado
    private Long tecnicoId;
    
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = new Date();
    }
}
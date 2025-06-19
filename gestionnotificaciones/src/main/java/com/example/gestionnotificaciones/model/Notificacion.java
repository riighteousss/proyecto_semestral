package com.example.gestionnotificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "Notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long usuarioId;
    
    private String mensaje;
    private Date fechaEnvio = new Date();
    private boolean leida = false;
    
    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = new Date();
    }
}

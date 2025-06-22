package com.example.gestionnotificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "Notificaciones")
@Schema(description = "Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincrementable")
    private Long id;
    @Schema(description = "llave foranea de usuario id")
    @Column(nullable = false)
    private Long usuarioId;
    @Schema(description = "mensaje de la notificacion")
    private String mensaje;
    @Schema(description = "fecha de envio, se genera automaticamente con la fecha actual")
    private Date fechaEnvio = new Date();
    @Schema(description = "indica si el mensaje fue leido o no, tipo de dato booleano(true, false)")
    private boolean leida = false;
    
    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = new Date();
    }
}

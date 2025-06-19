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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "asignaciones")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long tecnicoId;  // FK a usuario/tecnico
    
    @Column(nullable = false)
    private Long solicitudId; //FK a solicitud
    
    private Date fechaAsignacion = new Date();
    private String estado;
    
    @PrePersist
    protected void onCreate() {
        this.fechaAsignacion = new Date();
    }
}
package com.example.gestionsolicitudes.model;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "solicitud")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column()
    private Date fechasolicitud; 
    //tipo de solicitudes como mantenimiento, limpieza, formateo etc.
    @Column()
    private String tiposolicitud;
    @Column()
    private String descripciongeneral;
    
    @Column
    private Long idusuario;

   
    
    //Establece la fecha actual automáticamente
    @PrePersist
    protected void onCreate() {
        this.fechasolicitud = new Date(); 
    }

}

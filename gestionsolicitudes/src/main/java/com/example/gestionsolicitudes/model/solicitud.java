package com.example.gestionsolicitudes.model;
import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "solicitud")
public class solicitud extends RepresentationModel<solicitud>{
    @Schema(description = "ID autoincrementable")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Schema(description = "fecha solicitud, se rellena automaticamente con la fecha actual")
    @Column()
    private Date fechasolicitud; 
    //tipo de solicitudes como mantenimiento, limpieza, formateo etc.
    @Schema(description = "tipo de solicitudes como mantenimiento, limpieza, formateo etc.")
    @Column()
    private String tiposolicitud;
    @Schema(description = "descripcion general de la problematica")
    @Column()
    private String descripciongeneral;
    @Schema(description = "llave foranea de id usuario")
    @Column()
    private Long idusuario;
    @Schema(description = "llave foranea del equipo a reparar/revisar")
    @Column()
    private Long idequipo;

   
    
    //Establece la fecha actual automáticamente
    @PrePersist
    protected void onCreate() {
        this.fechasolicitud = new Date(); 
    }

}

package com.example.gestionreportes.Model;
import java.util.Date;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reporte")
@Entity
@Schema(description = "reporte")
public class reporte {
     @Schema(description = "ID autoincrementable")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Schema(description = "tipo de reporte")
    @Column()
    private String tiporeporte;
     @Schema(description = "descripcion general del reporte para dar mas informacion")
    @Column()
    private String descripciongeneral;
    @Schema(description = "id usuario(llave foranea)")
     @Column
    private Long idusuario;
    @Schema(description = "fecha de reporte, se registra automaticamente con la fecha actual al momento de crear el reporte")
    @Column
    private Date fechareporte;
   

     @PrePersist
    protected void onCreate() {
        this.fechareporte = new Date(); 
    }
}

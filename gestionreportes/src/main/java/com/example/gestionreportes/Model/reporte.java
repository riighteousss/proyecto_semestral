package com.example.gestionreportes.Model;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reporte")
@Entity
public class reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column()
    private String tiporeporte;
    @Column()
    private String descripciongeneral;
     @Column
    private Long idusuario;
    @Column
    private Date fechareporte;
   

     @PrePersist
    protected void onCreate() {
        this.fechareporte = new Date(); 
    }
}

package com.example.gestiontecnicos.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tecnico")
@Entity
@Schema(description = "tecnicos")
public class tecnicos extends RepresentationModel<tecnicos> {
    @Schema(description = "ID autoincrementable")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
     private long id;
     @Schema(description = "Rut atributo obligatorio y unico", required = true)
     @Column(nullable = false, unique = true)
     private String rut;
     @Schema(description = "nombre del tecnico")
     @Column(nullable = false)
     private String Nombre;
     @Schema(description = "especialidad del tecnico, puede ser agregado o dejado como null en el caso de no tener especialicacion en un area  ejemplos - Técnico de celulares,Especialista en micro soldadura etc.")
     @Column(nullable = true)
     private String especialidad;
     @Schema(description = "define si el tecnico esta habilitado(contrato vigente) o no")
     @Column()
     private boolean estado;
     

}

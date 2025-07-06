package com.example.equipos.model;

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

@Entity
@Table(name = "equipo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "modelo de equipo(dispositivos de un usuario)")
public class equipo extends RepresentationModel<equipo>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincrementable del equipo")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID del usuario propietario del equipo (llave foránea)", required = true)
    private Long idusuario;

    @Column()
    @Schema(description = "Tipo de dispositivo (Ej: teléfono, tablet, notebook, PC)", required = true)
    private String tipodispositivo;

    @Column()
    @Schema(description = "Marca del dispositivo (Ej: Dell, Samsung, Apple)", required = true)
    private String marca;

    @Column()
    @Schema(description = "Modelo del dispositivo (Ej: iphone 13, Galaxy S21, iPad)", required = true)
    private String modelo;

}

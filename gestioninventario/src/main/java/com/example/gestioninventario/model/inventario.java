package com.example.gestioninventario.model;

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
@Table(name = "Inventario")
@Entity
@Schema(description = "inventario")
public class inventario extends RepresentationModel<inventario>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincrementable")
    private Long id;
    
    @Schema(description = "nombre del objeto a guardar")
    @Column(nullable = false)
    private String nombre;
      @Schema(description = "descripcion general de lo que es")
    @Column(nullable = true)
    private String descripcion;
      @Schema(description = "cantidad disponible")
    @Column(nullable = false)
    private Integer stock = 0;
      @Schema(description = "cantidad minima disponible")
    @Column(nullable = true)
    private Integer stockMinimo = 0;
      @Schema(description = "categorizacion, indica que tipo de objeto es")
    @Column(nullable = true)
    private String categoria;
      @Schema(description = "unidad de medida/contabilizacion EJ: kg, piezas, unidades etc")
    @Column(nullable = true)
    private String unidadMedida; // unidad, pieza, kg, etc.
      @Schema(description = "precio del objeto")
    @Column(nullable = true)
    private Double precio;
      @Schema(description = "indica si el objeto esta activo o no para su uso.")
    @Column(nullable = false)
    private Boolean activo = true;
}

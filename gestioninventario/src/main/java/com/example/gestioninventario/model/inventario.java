package com.example.gestioninventario.model;

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
@Table(name = "inventario")
@Entity
public class inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = true)
    private String descripcion;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(nullable = true)
    private Integer stockMinimo = 0;
    
    @Column(nullable = true)
    private String categoria;
    
    @Column(nullable = true)
    private String unidadMedida; // unidad, pieza, kg, etc.
    
    @Column(nullable = true)
    private Double precio;
    
    @Column(nullable = false)
    private Boolean activo = true;
}

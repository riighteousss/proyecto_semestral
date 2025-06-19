package com.example.gestiontecnicos.model;

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
public class tecnicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
     private long id;
     @Column(nullable = false, unique = true)
     private String rut;
     @Column(nullable = false, unique = false)
     private String Nombre;
      @Column(nullable = true, unique = false)
     private String especialidad;
     

}

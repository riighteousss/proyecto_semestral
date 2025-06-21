package com.example.equipos.model;

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
public class equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    //llave foranea de idusuario para validar a qn pertenece el equipo
    private Long idusuario;
    //telefono, tablet, notebook, pc ..etc
    @Column()
    private String tipodispositivo;
    @Column()
    private String marca;
    @Column()
    private String modelo;

}

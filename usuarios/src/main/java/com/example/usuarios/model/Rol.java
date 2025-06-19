package com.example.usuarios.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto incrementable
    private long id;
    @Column(nullable = false, unique = true) // obligatorio y unico
    private String nombre;
    
    
    //identifico la relacion inversa: un rol puede tener muchos usuarios    
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    @JsonIgnore // no se incluyan los datos asociados a los roles
    List<usuario> users;





}


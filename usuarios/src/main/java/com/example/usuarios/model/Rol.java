package com.example.usuarios.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "Rol de usuario - Define los permisos y accesos del usuario en el sistema")
public class Rol {
    @Schema(description = "ID autoincrementable del rol")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Schema(description = "Nombre del rol", required = true, example = "ADMIN, TECNICO, USUARIO")
    @Column(nullable = false, unique = true)
    private String nombre;
    
    //identifico la relacion inversa: un rol puede tener muchos usuarios    
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    @JsonIgnore // no se incluyan los datos asociados a los roles
    @Schema(hidden = true) // Ocultar en la documentación de Swagger
    List<usuario> users;
}


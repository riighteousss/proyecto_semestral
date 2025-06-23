package com.example.usuarios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Usuario del sistema - Contiene la información básica y credenciales de acceso")
public class usuario {
    @Schema(description = "ID autoincrementable del usuario")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Schema(description = "Nombre de usuario único", required = true, example = "admin")
    @Column(nullable = false, unique = true)
    private String username;
    
    @Schema(description = "Contraseña del usuario", required = true, example = "password123")
    @Column(nullable = false)
    private String password;
    
    @Schema(description = "Correo electrónico único del usuario", required = true, example = "admin@example.com")
    @Column(nullable = false, unique = true)
    private String correo;
    
    @Schema(description = "Rol asignado al usuario")
    @ManyToOne
    @JoinColumn(name = "rol_id")
    @JsonIgnoreProperties("users")
    private Rol rol;
}

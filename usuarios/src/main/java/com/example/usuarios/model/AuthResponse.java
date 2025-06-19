package com.example.usuarios.model;  // En el mismo paquete que Usuario y Rol

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    private String username;
    private String rol;       // Nombre del rol (ej: "ADMIN", "TECNICO")
    private String mensaje;   // Ej: "Inicio de sesión exitoso"
    private String token;     // JWT token
}
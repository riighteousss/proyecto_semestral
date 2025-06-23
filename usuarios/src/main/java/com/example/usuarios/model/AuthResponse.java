package com.example.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Respuesta de autenticación - Contiene los datos del usuario autenticado y el token JWT")
public class AuthResponse {
    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;
    
    @Schema(description = "Rol del usuario", example = "ADMIN, TECNICO, USUARIO")
    private String rol;
    
    @Schema(description = "Mensaje de confirmación", example = "Inicio de sesión exitoso")
    private String mensaje;
    
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
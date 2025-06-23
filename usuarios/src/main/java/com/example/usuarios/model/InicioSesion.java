package com.example.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Datos de inicio de sesión")
public class InicioSesion {
    @Schema(description = "Nombre de usuario", required = true, example = "admin")
    private String username;
    
    @Schema(description = "Contraseña del usuario", required = true, example = "password123")
    private String password;
}


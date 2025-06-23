package com.example.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Datos para cambio de contraseña")
public class CambioContrasena {
    @Schema(description = "Nombre de usuario", required = true, example = "admin")
    private String username;
    
    @Schema(description = "Contraseña actual del usuario", required = true, example = "password123")
    private String contrasenaActual;
    
    @Schema(description = "Nueva contraseña", required = true, example = "newpassword456")
    private String contrasenaNueva;
    
    @Schema(description = "Confirmación de la nueva contraseña", required = true, example = "newpassword456")
    private String confirmarContrasena;
}
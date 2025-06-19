package com.example.usuarios.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CambioContrasena {
    private String username;
    private String contrasenaActual;
    private String contrasenaNueva;
    private String confirmarContrasena;
}
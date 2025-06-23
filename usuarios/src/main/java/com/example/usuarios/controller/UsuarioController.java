package com.example.usuarios.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.usuarios.model.AuthResponse;
import com.example.usuarios.model.CambioContrasena;
import com.example.usuarios.model.InicioSesion;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.usuario;
import com.example.usuarios.service.RoleService;
import com.example.usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "usuarios", description = "Operaciones relacionadas con la gestión de usuarios, autenticación y roles")
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Configurar según tus necesidades
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RoleService roleService;

    // ===== ENDPOINT PARA LOGIN CON TOKEN =====
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de login inválidos"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody InicioSesion loginRequest) {
        try {
            // Validación básica
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username y password son requeridos");
            }

            AuthResponse authResponse = usuarioService.autenticarUsuario(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
            );
            
            return ResponseEntity.ok(authResponse);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Error de autenticación", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", "Ocurrió un error inesperado"));
        }
    }

    // ===== ENDPOINT PARA CAMBIAR CONTRASEÑA =====
    @Operation(summary = "Cambiar contraseña", description = "Permite a un usuario cambiar su contraseña actual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados o contraseña actual incorrecta")
    })
    @PostMapping("/auth/change-password")
    public ResponseEntity<?> cambiarContrasena(@RequestBody CambioContrasena cambioContrasena) {
        try {
            String mensaje = usuarioService.cambiarContrasena(cambioContrasena);
            return ResponseEntity.ok(new SuccessResponse(mensaje));
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al cambiar contraseña", e.getMessage()));
        }
    }

    // ===== ENDPOINT PARA LOGOUT (OPCIONAL) =====
    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión del usuario (en JWT stateless se maneja en el frontend)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente")
    })
    @PostMapping("/auth/logout")
    public ResponseEntity<?> cerrarSesion(HttpServletRequest request) {
        // En JWT stateless, el logout se maneja en el frontend eliminando el token
        // Aquí podrías implementar una blacklist de tokens si es necesario
        return ResponseEntity.ok(new SuccessResponse("Sesión cerrada exitosamente"));
    }

    //endpoint para consultar todos los usuarios
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista con todos los usuarios registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
            content = @Content(schema = @Schema(implementation = usuario.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/users")
    public ResponseEntity<?> obtenerUsuarios(){
        try {
            List<usuario> users = usuarioService.buscarUsuarios();
            if(users.isEmpty()){
                return ResponseEntity.ok(new SuccessResponse("No hay usuarios registrados", users));
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener usuarios", e.getMessage()));
        }
    }

    //endpoint para obtener un usuario por su id
    @Operation(summary = "Buscar usuario por ID", description = "Devuelve los datos del usuario solicitado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente",
            content = @Content(schema = @Schema(implementation = usuario.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<?> obtenerusuario(@PathVariable Long id){
        try {
            usuario usuario = usuarioService.getUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno", e.getMessage()));
        }
    }

    //endpoint para consultar todos los roles
    @Operation(summary = "Obtener todos los roles", description = "Devuelve una lista con todos los roles disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida correctamente",
            content = @Content(schema = @Schema(implementation = Rol.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/roles")
    public ResponseEntity<?> obtenerRoles(){
        try {
            List<Rol> roles = roleService.buscarRoles();
            if(roles.isEmpty()){
                return ResponseEntity.ok(new SuccessResponse("No hay roles registrados", roles));
            }
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener roles", e.getMessage()));
        }
    }

    //endpoint para crear un nuevo usuario
    @Operation(summary = "Crear nuevo usuario", description = "Permite registrar un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
            content = @Content(schema = @Schema(implementation = usuario.class))),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados o usuario ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/users")
    public ResponseEntity<?> crearUsuario(@RequestBody usuario user){
        try {
            // Validación básica
            if (user.getRol() == null || user.getRol().getId() <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Datos inválidos", "El rol es requerido"));
            }

            usuario newuser = usuarioService.crearUsuario(
                user.getUsername(),
                user.getPassword(),
                user.getCorreo(),
                user.getRol().getId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(newuser);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al crear usuario", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

    //endpoint para eliminar usuario por su id
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarusuario(@PathVariable Long id){
        try {
            String mensaje = usuarioService.eliminarusuarioporid(id);
            return ResponseEntity.ok(new SuccessResponse(mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Error al eliminar usuario", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

    //endpoint para actualizar datos de usuario
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
            content = @Content(schema = @Schema(implementation = usuario.class))),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody usuario datosnuevos) {
        try {
            usuario usuarioModificado = usuarioService.actualizarUsuario(id, datosnuevos);
            return ResponseEntity.ok(usuarioModificado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al actualizar usuario", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

    // Clases para respuestas estandarizadas
    @Schema(description = "Respuesta de error estandarizada")
    public static class ErrorResponse {
        @Schema(description = "Tipo de error", example = "Error de autenticación")
        private String error;
        @Schema(description = "Mensaje descriptivo del error", example = "Credenciales incorrectas")
        private String mensaje;
        @Schema(description = "Timestamp del error", example = "1640995200000")
        private long timestamp;

        public ErrorResponse(String error, String mensaje) {
            this.error = error;
            this.mensaje = mensaje;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getError() { return error; }
        public String getMensaje() { return mensaje; }
        public long getTimestamp() { return timestamp; }
    }

    @Schema(description = "Respuesta de éxito estandarizada")
    public static class SuccessResponse {
        @Schema(description = "Mensaje de éxito", example = "Operación completada exitosamente")
        private String mensaje;
        @Schema(description = "Datos adicionales (opcional)")
        private Object data;
        @Schema(description = "Timestamp de la respuesta", example = "1640995200000")
        private long timestamp;

        public SuccessResponse(String mensaje) {
            this.mensaje = mensaje;
            this.timestamp = System.currentTimeMillis();
        }

        public SuccessResponse(String mensaje, Object data) {
            this.mensaje = mensaje;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getMensaje() { return mensaje; }
        public Object getData() { return data; }
        public long getTimestamp() { return timestamp; }
    }
}
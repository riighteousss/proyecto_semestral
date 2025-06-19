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

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Configurar según tus necesidades
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RoleService roleService;

    // ===== ENDPOINT PARA LOGIN CON TOKEN =====
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
    @PostMapping("/auth/logout")
    public ResponseEntity<?> cerrarSesion(HttpServletRequest request) {
        // En JWT stateless, el logout se maneja en el frontend eliminando el token
        // Aquí podrías implementar una blacklist de tokens si es necesario
        return ResponseEntity.ok(new SuccessResponse("Sesión cerrada exitosamente"));
    }

    //endpoint para consultar todos los usuarios
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
    public static class ErrorResponse {
        private String error;
        private String mensaje;
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

    public static class SuccessResponse {
        private String mensaje;
        private Object data;
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
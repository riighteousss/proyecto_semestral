package com.example.usuarios.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.usuarios.model.AuthResponse;
import com.example.usuarios.model.CambioContrasena;
import com.example.usuarios.model.InicioSesion;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.usuario;
import com.example.usuarios.service.JwtUtil;
import com.example.usuarios.service.RoleService;
import com.example.usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "usuarios", description = "Operaciones relacionadas con la gestión de usuarios, autenticación y roles")
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtUtil jwtUtil;

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
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("Username y password son requeridos");
            }

            AuthResponse authResponse = usuarioService.autenticarUsuario(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
            );
            
            EntityModel<AuthResponse> authModel = EntityModel.of(authResponse);
            authModel.add(linkTo(methodOn(UsuarioController.class).cambiarContrasena(null)).withRel("change-password"));
            authModel.add(linkTo(methodOn(UsuarioController.class).cerrarSesion(null)).withRel("logout"));
            authModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("users"));
            
            return ResponseEntity.ok(authModel);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Error de autenticación", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", "Ocurrió un error inesperado"));
        }
    }

    @Operation(summary = "Cambiar contraseña", description = "Permite a un usuario cambiar su contraseña actual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados o contraseña actual incorrecta")
    })
    @PostMapping("/auth/change-password")
    public ResponseEntity<?> cambiarContrasena(@RequestBody CambioContrasena cambioContrasena) {
        try {
            String mensaje = usuarioService.cambiarContrasena(cambioContrasena);
            
            SuccessResponse response = new SuccessResponse(mensaje);
            EntityModel<SuccessResponse> responseModel = EntityModel.of(response);
            responseModel.add(linkTo(methodOn(UsuarioController.class).iniciarSesion(null)).withRel("login"));
            responseModel.add(linkTo(methodOn(UsuarioController.class).cerrarSesion(null)).withRel("logout"));
            
            return ResponseEntity.ok(responseModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al cambiar contraseña", e.getMessage()));
        }
    }

    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente")
    })
    @PostMapping("/auth/logout")
    public ResponseEntity<?> cerrarSesion(HttpServletRequest request) {
        SuccessResponse response = new SuccessResponse("Sesión cerrada exitosamente");
        EntityModel<SuccessResponse> responseModel = EntityModel.of(response);
        responseModel.add(linkTo(methodOn(UsuarioController.class).iniciarSesion(null)).withRel("login"));
        
        return ResponseEntity.ok(responseModel);
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista con todos los usuarios registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/users")
    public ResponseEntity<?> obtenerUsuarios(HttpServletRequest request) {
        try {
            String token = jwtUtil.extraerTokenDelHeader(request.getHeader("Authorization"));
            if (token == null || !jwtUtil.esTokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Acceso no autorizado", "Token inválido o faltante"));
            }

            List<usuario> users = usuarioService.buscarUsuarios();
            
            if(users.isEmpty()){
                // Crear un mapa para la respuesta
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No hay usuarios registrados");
                response.put("data", Collections.emptyList());
                
                // Crear el modelo HATEOAS
                EntityModel<Map<String, Object>> model = EntityModel.of(response);
                model.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withSelfRel());
                model.add(linkTo(methodOn(UsuarioController.class).crearUsuario(null, null)).withRel("create-user"));
                model.add(linkTo(methodOn(UsuarioController.class).obtenerRoles(null)).withRel("roles"));
                
                return ResponseEntity.ok(model);
            }

            List<EntityModel<usuario>> userModels = users.stream()
                .map(user -> {
                    EntityModel<usuario> userModel = EntityModel.of(user);
                    userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuario(user.getId(), null)).withSelfRel());
                    userModel.add(linkTo(methodOn(UsuarioController.class).actualizarUsuario(user.getId(), null, null)).withRel("update"));
                    userModel.add(linkTo(methodOn(UsuarioController.class).eliminarUsuario(user.getId(), null)).withRel("delete"));
                    return userModel;
                })
                .collect(Collectors.toList());

            CollectionModel<EntityModel<usuario>> collectionModel = CollectionModel.of(userModels);
            collectionModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withSelfRel());
            collectionModel.add(linkTo(methodOn(UsuarioController.class).crearUsuario(null, null)).withRel("create-user"));
            collectionModel.add(linkTo(methodOn(UsuarioController.class).obtenerRoles(null)).withRel("roles"));

            return ResponseEntity.ok(collectionModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener usuarios", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar usuario por ID", description = "Devuelve los datos del usuario solicitado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = jwtUtil.extraerTokenDelHeader(request.getHeader("Authorization"));
            if (token == null || !jwtUtil.esTokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Acceso no autorizado", "Token inválido o faltante"));
            }

            usuario usuario = usuarioService.getUsuario(id);
            
            EntityModel<usuario> userModel = EntityModel.of(usuario);
            userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuario(id, null)).withSelfRel());
            userModel.add(linkTo(methodOn(UsuarioController.class).actualizarUsuario(id, null, null)).withRel("update"));
            userModel.add(linkTo(methodOn(UsuarioController.class).eliminarUsuario(id, null)).withRel("delete"));
            userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("all-users"));
            
            return ResponseEntity.ok(userModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener todos los roles", description = "Devuelve una lista con todos los roles disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/roles")
    public ResponseEntity<?> obtenerRoles(HttpServletRequest request) {
        try {
            String token = jwtUtil.extraerTokenDelHeader(request.getHeader("Authorization"));
            if (token == null || !jwtUtil.esTokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Acceso no autorizado", "Token inválido o faltante"));
            }

            List<Rol> roles = roleService.buscarRoles();
            
            if(roles.isEmpty()){
                SuccessResponse response = new SuccessResponse("No hay roles registrados", roles);
                EntityModel<SuccessResponse> responseModel = EntityModel.of(response);
                responseModel.add(linkTo(methodOn(UsuarioController.class).obtenerRoles(null)).withSelfRel());
                responseModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("users"));
                return ResponseEntity.ok(responseModel);
            }

            List<EntityModel<Rol>> roleModels = roles.stream()
                .map(rol -> {
                    EntityModel<Rol> roleModel = EntityModel.of(rol);
                    roleModel.add(linkTo(methodOn(UsuarioController.class).obtenerRoles(null)).withRel("all-roles"));
                    return roleModel;
                })
                .collect(Collectors.toList());

            CollectionModel<EntityModel<Rol>> collectionModel = CollectionModel.of(roleModels);
            collectionModel.add(linkTo(methodOn(UsuarioController.class).obtenerRoles(null)).withSelfRel());
            collectionModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("users"));

            return ResponseEntity.ok(collectionModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener roles", e.getMessage()));
        }
    }

    @Operation(summary = "Crear nuevo usuario", description = "Permite registrar un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/users")
    public ResponseEntity<?> crearUsuario(@RequestBody usuario user, HttpServletRequest request) {
        try {
            String token = jwtUtil.extraerTokenDelHeader(request.getHeader("Authorization"));
            if (token == null || !jwtUtil.esTokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Acceso no autorizado", "Token inválido o faltante"));
            }
            
            String rol = jwtUtil.obtenerRol(token);
            if (!"ADMIN".equals(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("Acceso denegado", "Se requiere rol de ADMIN"));
            }

            if (user.getRol() == null || user.getRol().getId() <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Datos inválidos", "El rol es requerido"));
            }

            usuario newUser = usuarioService.crearUsuario(
                user.getUsername(),
                user.getPassword(),
                user.getCorreo(),
                user.getRol().getId()
            );

            EntityModel<usuario> userModel = EntityModel.of(newUser);
            userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuario(newUser.getId(), null)).withSelfRel());
            userModel.add(linkTo(methodOn(UsuarioController.class).actualizarUsuario(newUser.getId(), null, null)).withRel("update"));
            userModel.add(linkTo(methodOn(UsuarioController.class).eliminarUsuario(newUser.getId(), null)).withRel("delete"));
            userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("all-users"));

            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al crear usuario", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = jwtUtil.extraerTokenDelHeader(request.getHeader("Authorization"));
            if (token == null || !jwtUtil.esTokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Acceso no autorizado", "Token inválido o faltante"));
            }
            
            String rol = jwtUtil.obtenerRol(token);
            if (!"ADMIN".equals(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("Acceso denegado", "Se requiere rol de ADMIN"));
            }

            String mensaje = usuarioService.eliminarusuarioporid(id);
            
            SuccessResponse response = new SuccessResponse(mensaje);
            EntityModel<SuccessResponse> responseModel = EntityModel.of(response);
            responseModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("all-users"));
            responseModel.add(linkTo(methodOn(UsuarioController.class).crearUsuario(null, null)).withRel("create-user"));
            
            return ResponseEntity.ok(responseModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Error al eliminar usuario", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody usuario datosnuevos, HttpServletRequest request) {
        try {
            String token = jwtUtil.extraerTokenDelHeader(request.getHeader("Authorization"));
            if (token == null || !jwtUtil.esTokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Acceso no autorizado", "Token inválido o faltante"));
            }

            String rol = jwtUtil.obtenerRol(token);
            if (!"ADMIN".equals(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("Acceso denegado", "Se requiere rol de ADMIN"));
            }

            usuario usuarioModificado = usuarioService.actualizarUsuario(id, datosnuevos);
            
            EntityModel<usuario> userModel = EntityModel.of(usuarioModificado);
            userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuario(id, null)).withSelfRel());
            userModel.add(linkTo(methodOn(UsuarioController.class).eliminarUsuario(id, null)).withRel("delete"));
            userModel.add(linkTo(methodOn(UsuarioController.class).obtenerUsuarios(null)).withRel("all-users"));
            
            return ResponseEntity.ok(userModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al actualizar usuario", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

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

        public String getMensaje() { return mensaje; }
        public Object getData() { return data; }
        public long getTimestamp() { return timestamp; }
    }
}
package com.example.usuarios;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.usuarios.controller.UsuarioController;
import com.example.usuarios.model.AuthResponse;
import com.example.usuarios.model.CambioContrasena;
import com.example.usuarios.model.InicioSesion;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.usuario;
import com.example.usuarios.service.RoleService;
import com.example.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
@Import(TestSecurityConfig.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private com.example.usuarios.service.JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private usuario testUser;
    private Rol testRol;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        testRol = new Rol();
        testRol.setId(1L);
        testRol.setNombre("ADMIN");

        testUser = new usuario();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setCorreo("test@example.com");
        testUser.setRol(testRol);

        authResponse = new AuthResponse(
            "testuser", 
            "ADMIN", 
            "Login exitoso", 
            "token.jwt.here"
        );
    }

    @Test
    void testLoginSuccess() throws Exception {
        InicioSesion loginRequest = new InicioSesion("testuser", "password");
        
        when(usuarioService.autenticarUsuario(anyString(), anyString()))
            .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.rol").value("ADMIN"))
            .andExpect(jsonPath("$.mensaje").value("Login exitoso"))
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginWithNullUsername() throws Exception {
        InicioSesion loginRequest = new InicioSesion(null, "password");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        InicioSesion loginRequest = new InicioSesion("wronguser", "wrongpass");
        
        when(usuarioService.autenticarUsuario(anyString(), anyString()))
            .thenThrow(new RuntimeException("Credenciales inválidas"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Error de autenticación"))
            .andExpect(jsonPath("$.mensaje").value("Credenciales inválidas"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById() throws Exception {
        when(usuarioService.getUsuario(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.correo").value("test@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserNotFound() throws Exception {
        when(usuarioService.getUsuario(999L))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(get("/api/v1/users/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Usuario no encontrado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        List<usuario> users = Arrays.asList(testUser);
        when(usuarioService.buscarUsuarios()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].username").value("testuser"))
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsersEmpty() throws Exception {
        when(usuarioService.buscarUsuarios()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("No hay usuarios registrados"))
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllRoles() throws Exception {
        List<Rol> roles = Arrays.asList(testRol);
        when(roleService.buscarRoles()).thenReturn(roles);

        mockMvc.perform(get("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nombre").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUser() throws Exception {
        usuario userRequest = new usuario();
        userRequest.setUsername("newuser");
        userRequest.setCorreo("newuser@example.com");
        userRequest.setPassword("newpassword");
        userRequest.setRol(testRol); // Agregar el rol requerido

        when(usuarioService.crearUsuario(anyString(), anyString(), anyString(), anyLong()))
            .thenReturn(testUser);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserWithoutRole() throws Exception {
        usuario userRequest = new usuario();
        userRequest.setUsername("newuser");
        userRequest.setCorreo("newuser@example.com");
        userRequest.setPassword("newpassword");
        // Sin rol o con rol inválido

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Datos inválidos"))
            .andExpect(jsonPath("$.mensaje").value("El rol es requerido"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserDuplicateUsername() throws Exception {
        usuario userRequest = new usuario();
        userRequest.setUsername("existinguser");
        userRequest.setCorreo("new@example.com");
        userRequest.setPassword("password");
        userRequest.setRol(testRol);

        when(usuarioService.crearUsuario(anyString(), anyString(), anyString(), anyLong()))
            .thenThrow(new RuntimeException("El nombre de usuario existinguser ya está en uso"));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Error al crear usuario"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUser() throws Exception {
        usuario updatedUser = new usuario();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setCorreo("updated@example.com");
        updatedUser.setRol(testRol);

        when(usuarioService.actualizarUsuario(eq(1L), any(usuario.class)))
            .thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("updateduser"))
            .andExpect(jsonPath("$.correo").value("updated@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUserNotFound() throws Exception {
        usuario updatedUser = new usuario();
        updatedUser.setUsername("updateduser");
        updatedUser.setCorreo("updated@example.com");
        updatedUser.setRol(testRol);

        when(usuarioService.actualizarUsuario(eq(999L), any(usuario.class)))
            .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        mockMvc.perform(put("/api/v1/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Error al actualizar usuario"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        when(usuarioService.eliminarusuarioporid(1L))
            .thenReturn("El usuario con ID 1 se ha eliminado exitosamente");

        mockMvc.perform(delete("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("El usuario con ID 1 se ha eliminado exitosamente"));
        
        verify(usuarioService, times(1)).eliminarusuarioporid(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserNotFound() throws Exception {
        when(usuarioService.eliminarusuarioporid(999L))
            .thenThrow(new RuntimeException("El usuario con ID 999 no existe"));

        mockMvc.perform(delete("/api/v1/users/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Error al eliminar usuario"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testChangePassword() throws Exception {
        CambioContrasena cambio = new CambioContrasena();
        cambio.setUsername("testuser");
        cambio.setContrasenaActual("oldpass");
        cambio.setContrasenaNueva("newpass");
        cambio.setConfirmarContrasena("newpass");

        when(usuarioService.cambiarContrasena(any(CambioContrasena.class)))
            .thenReturn("Contraseña actualizada exitosamente");

        mockMvc.perform(post("/api/v1/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cambio)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("Contraseña actualizada exitosamente"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testChangePasswordMismatch() throws Exception {
        CambioContrasena cambio = new CambioContrasena();
        cambio.setUsername("testuser");
        cambio.setContrasenaActual("oldpass");
        cambio.setContrasenaNueva("newpass");
        cambio.setConfirmarContrasena("differentpass");

        when(usuarioService.cambiarContrasena(any(CambioContrasena.class)))
            .thenThrow(new RuntimeException("Las contraseñas nuevas no coinciden"));

        mockMvc.perform(post("/api/v1/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cambio)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Error al cambiar contraseña"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("Sesión cerrada exitosamente"));
    }

    // Test para verificar manejo de errores internos
    @Test
    @WithMockUser(roles = "ADMIN")
    void testInternalServerError() throws Exception {
        when(usuarioService.buscarUsuarios())
            .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("Error al obtener usuarios"));
    }
}
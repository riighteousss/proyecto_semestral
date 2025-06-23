package com.example.usuarios;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.hamcrest.Matchers.*;

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
import com.example.usuarios.model.usuario;
import com.example.usuarios.model.Rol;
import com.example.usuarios.service.UsuarioService;
import com.example.usuarios.service.RoleService;
import com.example.usuarios.service.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
@Import(TestSecurityConfig.class)
public class UsuarioControllerHateoasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtUtil jwtUtil;

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
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsersEmptyWithHateoas() throws Exception {
        when(usuarioService.buscarUsuarios()).thenReturn(Arrays.asList());
        when(jwtUtil.extraerTokenDelHeader(anyString())).thenReturn("valid-token");
        when(jwtUtil.esTokenValido(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer valid-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded").doesNotExist())
            .andExpect(jsonPath("$.mensaje").value("No hay usuarios registrados"))
            .andExpect(jsonPath("$._links").exists())
            .andExpect(jsonPath("$._links.self").exists())
            .andExpect(jsonPath("$._links.create-user").exists())
            .andExpect(jsonPath("$._links.roles").exists())
            .andExpect(jsonPath("$._links.self.href", containsString("/users")))
            .andExpect(jsonPath("$._links.create-user.href", containsString("/users")))
            .andExpect(jsonPath("$._links.roles.href", containsString("/roles")));
    }
}

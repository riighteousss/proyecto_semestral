package com.example.usuarios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.usuarios.model.AuthResponse;
import com.example.usuarios.model.CambioContrasena;
import com.example.usuarios.model.Rol;
import com.example.usuarios.model.usuario;
import com.example.usuarios.repository.RoleRepository;
import com.example.usuarios.repository.UsuarioRepository;
import com.example.usuarios.service.JwtUtil;
import com.example.usuarios.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private usuario testUser;
    private Rol testRol;

    @BeforeEach
    void setUp() {
        testRol = new Rol();
        testRol.setId(1L);
        testRol.setNombre("ADMIN");

        testUser = new usuario();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setCorreo("test@example.com");
        testUser.setRol(testRol);
    }

    @Test
    void testAutenticarUsuarioSuccess() {
        when(usuarioRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "encodedPassword"))
            .thenReturn(true);
        when(jwtUtil.generarToken(anyString(), anyString()))
            .thenReturn("token.jwt.here");

        AuthResponse response = usuarioService.autenticarUsuario("testuser", "password");

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("ADMIN", response.getRol());
    }

    @Test
    void testCrearUsuarioSuccess() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByCorreo("test@example.com")).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRol));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(usuario.class))).thenReturn(testUser);

        usuario result = usuarioService.crearUsuario(
            "testuser", "password", "test@example.com", 1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUsuarioById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUser));

        usuario result = usuarioService.getUsuario(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCambiarContrasenaSuccess() {
        CambioContrasena cambio = new CambioContrasena();
        cambio.setUsername("testuser");
        cambio.setContrasenaActual("oldpass");
        cambio.setContrasenaNueva("newpass");
        cambio.setConfirmarContrasena("newpass");

        when(usuarioRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldpass", "encodedPassword"))
            .thenReturn(true);
        when(passwordEncoder.encode("newpass")).thenReturn("newEncodedPassword");

        String result = usuarioService.cambiarContrasena(cambio);

        assertEquals("Contraseña actualizada exitosamente", result);
        verify(usuarioRepository).save(any(usuario.class));
    }

    @Test
    void testActualizarUsuario() {
        usuario datosNuevos = new usuario();
        datosNuevos.setUsername("newuser");
        datosNuevos.setCorreo("new@example.com");
        datosNuevos.setRol(testRol);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRol));
        when(usuarioRepository.save(any(usuario.class))).thenReturn(testUser);

        usuario result = usuarioService.actualizarUsuario(1L, datosNuevos);

        assertNotNull(result);
        verify(usuarioRepository).save(any(usuario.class));
    }
}
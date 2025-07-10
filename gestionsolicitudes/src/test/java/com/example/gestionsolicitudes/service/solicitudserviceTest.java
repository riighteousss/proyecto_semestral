package com.example.gestionsolicitudes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import com.example.gestionsolicitudes.model.solicitud;
import com.example.gestionsolicitudes.repository.solicitudrepository;
import com.example.gestionsolicitudes.services.solicitudservice;
import com.example.gestionsolicitudes.webclient.equipoclient;
import com.example.gestionsolicitudes.webclient.usuarioclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class solicitudserviceTest {

    @Mock
    private solicitudrepository repository;

    @Mock
    private usuarioclient usuarioClient;

    @Mock
    private equipoclient equipoClient;

    @InjectMocks
    private solicitudservice service;

    // Test buscarsolicitudes (con resultados)
    @Test
    public void testBuscarSolicitudes_ConResultados() {
        List<solicitud> lista = Arrays.asList(new solicitud(), new solicitud());

        when(repository.findAll()).thenReturn(lista);

        List<solicitud> resultado = service.buscarsolicitudes();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // Test buscarporid (existe)
    @Test
    public void testBuscarPorId_Existe() {
        solicitud sol = new solicitud();
        sol.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(sol));

        solicitud resultado = service.buscarporid(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    // Test buscarporid (no existe)
    @Test
    public void testBuscarPorId_NoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarporid(1L);
        });

        assertTrue(ex.getMessage().contains("Solicitud no encontrada"));
    }

    // Test buscarporidusuario (con resultados)
    @Test
    public void testBuscarPorIdUsuario_ConResultados() {
        List<solicitud> lista = Arrays.asList(new solicitud(), new solicitud());

        when(repository.findByIdusuario(1L)).thenReturn(lista);

        List<solicitud> resultado = service.buscarporidusuario(1L);

        assertEquals(2, resultado.size());
    }

    // Test buscarporidusuario (sin resultados)
    @Test
    public void testBuscarPorIdUsuario_SinResultados() {
        when(repository.findByIdusuario(1L)).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarporidusuario(1L);
        });

        assertTrue(ex.getMessage().contains("No se encontraron solicitudes"));
    }

   // Test crearsolicitud (usuario y equipo válidos)
@Test
public void testCrearSolicitud_Valido() {
    Map<String, Object> usuarioMock = new HashMap<>();
    usuarioMock.put("id", 1L);

    Map<String, Object> equipoMock = new HashMap<>();
    equipoMock.put("id", 10L);

    solicitud nueva = new solicitud();
    nueva.setId(1L);
    nueva.setIdusuario(1L);
    nueva.setIdequipo(10L);
    nueva.setTiposolicitud("Soporte");
    nueva.setDescripciongeneral("Descripción");

    String token = "token-de-prueba";

    when(usuarioClient.obtenerUsuarioPorId(1L, token)).thenReturn(usuarioMock);
    when(equipoClient.obtenerequipoid(10L)).thenReturn(equipoMock);
    when(repository.save(any(solicitud.class))).thenReturn(nueva);

    solicitud resultado = service.crearsolicitud("Soporte", "Descripción", 1L, 10L, token);

    assertNotNull(resultado);
    assertEquals("Soporte", resultado.getTiposolicitud());
}

// Test crearsolicitud (usuario no encontrado)
@Test
public void testCrearSolicitud_UsuarioNoEncontrado() {
    String token = "token-de-prueba";

    when(usuarioClient.obtenerUsuarioPorId(1L, token)).thenReturn(null);

    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
        service.crearsolicitud("Soporte", "desc", 1L, 10L, token);
    });

    assertTrue(ex.getMessage().contains("Usuario no encontrado"));
}

// Test crearsolicitud (equipo no encontrado)
@Test
public void testCrearSolicitud_EquipoNoEncontrado() {
    Map<String, Object> usuarioMock = new HashMap<>();
    usuarioMock.put("id", 1L);

    String token = "token-de-prueba";

    when(usuarioClient.obtenerUsuarioPorId(1L, token)).thenReturn(usuarioMock);
    when(equipoClient.obtenerequipoid(10L)).thenReturn(null);

    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
        service.crearsolicitud("Soporte", "desc", 1L, 10L, token);
    });

    assertTrue(ex.getMessage().contains("equipo no encontrado"));
}

    // Test eliminarsolicitudporid (existe)
    @Test
    public void testEliminarSolicitud_Existe() {
        when(repository.existsById(1L)).thenReturn(true);

        String resultado = service.eliminarsolicitudporid(1L);

        assertTrue(resultado.contains("eliminado exitosamente"));
        verify(repository, times(1)).deleteById(1L);
    }

    // Test eliminarsolicitudporid (no existe)
    @Test
    public void testEliminarSolicitud_NoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.eliminarsolicitudporid(1L);
        });

        assertTrue(ex.getMessage().contains("no existe"));
    }

    // Test actualizarporusuario (válido)
    @Test
    public void testActualizarPorUsuario_Valido() {
        solicitud existente = new solicitud();
        existente.setId(1L);
        existente.setIdusuario(5L);
        existente.setTiposolicitud("Antiguo");
        existente.setDescripciongeneral("Vieja desc");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(solicitud.class))).thenReturn(existente);

        solicitud resultado = service.actualizarporusuario(1L, 5L, "Nuevo", "Nueva desc");

        assertEquals("Nuevo", resultado.getTiposolicitud());
        assertEquals("Nueva desc", resultado.getDescripciongeneral());
    }

    // Test actualizarporusuario (solicitud no existe)
    @Test
    public void testActualizarPorUsuario_NoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarporusuario(1L, 5L, "Nuevo", "Nueva desc");
        });

        assertTrue(ex.getMessage().contains("no encontrada"));
    }

    // Test actualizarporusuario (no pertenece al usuario)
    @Test
    public void testActualizarPorUsuario_NoPertenece() {
        solicitud existente = new solicitud();
        existente.setId(1L);
        existente.setIdusuario(9L); // distinto

        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarporusuario(1L, 5L, "Nuevo", "Nueva desc");
        });

        assertTrue(ex.getMessage().contains("no pertenece al usuario"));
    }

    // Test eliminarporid (existe)
    @Test
    public void testEliminarPorId_Existe() {
        when(repository.existsById(1L)).thenReturn(true);

        String resultado = service.eliminarporid(1L);

        assertTrue(resultado.contains("se ah eliminado exitosamente"));
        verify(repository, times(1)).deleteById(1L);
    }

    // Test eliminarporid (no existe)
    @Test
    public void testEliminarPorId_NoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.eliminarporid(1L);
        });

        assertTrue(ex.getMessage().contains("No existe"));
    }
}

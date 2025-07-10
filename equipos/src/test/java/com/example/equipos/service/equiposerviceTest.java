package com.example.equipos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.equipos.model.equipo;
import com.example.equipos.repository.EquipoRepository;
import com.example.equipos.webclient.usuarioclient;

@ExtendWith(MockitoExtension.class)
public class equiposerviceTest {
    @Mock
    private EquipoRepository equiporepository;

    @Mock
    private usuarioclient usuarioClient;

    @InjectMocks
    private EquipoService service;

    @Test
    public void testBuscarquipos_RetornaLista() {
        List<equipo> listaMock = Arrays.asList(
            new equipo(1L, 1L, "Laptop", "Dell", "XPS"),
            new equipo(2L, 2L, "Celular", "Samsung", "S21")
        );

        when(equiporepository.findAll()).thenReturn(listaMock);

        List<equipo> resultado = service.buscarquipos();

        assertEquals(2, resultado.size());
        verify(equiporepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId() {
        equipo equipoMock = new equipo();
        equipoMock.setId(1L);

        when(equiporepository.findById(1L)).thenReturn(Optional.of(equipoMock));

        equipo resultado = service.buscarporid(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testBuscarPorId_NoExiste() {
        when(equiporepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarporid(1L);
        });

        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test
    public void testBuscarPorIdUsuario_ConResultados() {
        equipo equipo1 = new equipo();
        equipo equipo2 = new equipo();

        List<equipo> lista = Arrays.asList(equipo1, equipo2);

        when(equiporepository.findByIdusuario(1L)).thenReturn(lista);

        List<equipo> resultado = service.buscarporidusuario(1L);

        assertEquals(2, resultado.size());
    }

    @Test
    public void testBuscarPorIdUsuario_SinResultados() {
        when(equiporepository.findByIdusuario(1L)).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarporidusuario(1L);
        });

        assertTrue(ex.getMessage().contains("No se encontraron equipos"));
    }

    @Test
    public void testAgregarEquipo_UsuarioEncontrado() {
        Map<String, Object> usuarioMock = new HashMap<>();
        usuarioMock.put("id", 1L);

        when(usuarioClient.obtenerUsuarioPorId(1L,"tokenDePrueba")).thenReturn(usuarioMock);

        equipo equipoGuardado = new equipo();
        equipoGuardado.setIdusuario(1L);
        equipoGuardado.setTipodispositivo("Laptop");
        equipoGuardado.setMarca("HP");
        equipoGuardado.setModelo("Elitebook");

        when(equiporepository.save(any(equipo.class))).thenReturn(equipoGuardado);

        equipo resultado = service.agregarequipo(1L,"tokenDePrueba", "Laptop", "HP", "Elitebook");

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getTipodispositivo());
    }

    @Test
    public void testAgregarEquipo_UsuarioNoEncontrado() {
        when(usuarioClient.obtenerUsuarioPorId(1L,"tokenDePrueba")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.agregarequipo(1L,"tokenDePrueba", "Tablet", "Samsung", "Galaxy Tab");
        });

        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    public void testEliminarEquipo_Existe() {
        when(equiporepository.existsById(1L)).thenReturn(true);

        String resultado = service.eliminarequipoporid(1L);

        assertTrue(resultado.contains("se ha eliminado correctamente"));
        verify(equiporepository, times(1)).deleteById(1L);
    }

    @Test
    public void testEliminarEquipo_NoExiste() {
        when(equiporepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.eliminarequipoporid(1L);
        });

        assertTrue(ex.getMessage().contains("no existe"));
    }
}
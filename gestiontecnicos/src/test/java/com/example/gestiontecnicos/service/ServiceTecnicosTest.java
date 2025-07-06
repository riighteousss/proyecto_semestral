package com.example.gestiontecnicos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.gestiontecnicos.model.tecnicos;
import com.example.gestiontecnicos.repository.RepositoryTecnicos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceTecnicosTest {

    @Mock
    private RepositoryTecnicos repository;

    @InjectMocks
    private ServiceTecnicos service;

    // Test BuscarTecnicos (con resultados)
    @Test
    public void testBuscarTecnicos_ConResultados() {
        List<tecnicos> listaMock = Arrays.asList(
            new tecnicos(1L, "12345678-9", "Juan Pérez", "Electricidad", true),
            new tecnicos(2L, "98765432-1", "Ana López", "Plomería", true)
        );

        when(repository.findAll()).thenReturn(listaMock);

        List<tecnicos> resultado = service.BuscarTecnicos();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // Test buscarporid (existe)
    @Test
    public void testBuscarPorId_Existe() {
        tecnicos tecnico = new tecnicos(1L, "12345678-9", "Juan Pérez", "Electricidad", true);

        when(repository.findById(1L)).thenReturn(Optional.of(tecnico));

        tecnicos resultado = service.buscarporid(1L);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
    }

    // Test buscarporid (no existe)
    @Test
    public void testBuscarPorId_NoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarporid(1L);
        });

        assertTrue(ex.getMessage().contains("no existe"));
    }

    // Test AgregarTecnico (nuevo técnico)
    @Test
    public void testAgregarTecnico_Nuevo() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.empty());

        tecnicos tecnicoGuardado = new tecnicos();
        tecnicoGuardado.setRut("12345678-9");
        tecnicoGuardado.setNombre("Juan Pérez");
        tecnicoGuardado.setEspecialidad("Electricidad");
        tecnicoGuardado.setEstado(true);

        when(repository.save(any(tecnicos.class))).thenReturn(tecnicoGuardado);

        tecnicos resultado = service.AgregarTecnico("12345678-9", "Juan Pérez", "Electricidad");

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
    }

    // Test AgregarTecnico (ya existe)
    @Test
    public void testAgregarTecnico_YaExiste() {
        tecnicos existente = new tecnicos();
        existente.setRut("12345678-9");

        when(repository.findByRut("12345678-9")).thenReturn(Optional.of(existente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.AgregarTecnico("12345678-9", "Juan Pérez", "Electricidad");
        });

        assertTrue(ex.getMessage().contains("ya existe"));
    }

    // Test actualizarEspecializacion (existe)
    @Test
    public void testActualizarEspecializacion_Existe() {
        tecnicos tecnico = new tecnicos();
        tecnico.setId(1L);
        tecnico.setEspecialidad("Antigua");

        when(repository.findById(1L)).thenReturn(Optional.of(tecnico));
        when(repository.save(any(tecnicos.class))).thenReturn(tecnico);

        tecnicos resultado = service.actualizarEspecializacion(1L, "Nueva");

        assertEquals("Nueva", resultado.getEspecialidad());
    }

    // Test actualizarEspecializacion (no existe)
    @Test
    public void testActualizarEspecializacion_NoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarEspecializacion(1L, "Nueva");
        });

        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    // Test actualizarestado (existe)
    @Test
    public void testActualizarEstado_Existe() {
        tecnicos tecnico = new tecnicos();
        tecnico.setId(1L);
        tecnico.setEstado(true);

        when(repository.findById(1L)).thenReturn(Optional.of(tecnico));
        when(repository.save(any(tecnicos.class))).thenReturn(tecnico);

        tecnicos resultado = service.actualizarestado(1L, false);

        assertFalse(resultado.isEstado());
    }

    // Test actualizarestado (no existe)
    @Test
    public void testActualizarEstado_NoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarestado(1L, false);
        });

        assertTrue(ex.getMessage().contains("no encontrado"));
    }
}
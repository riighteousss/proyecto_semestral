package com.example.gestionreportes.service;

import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Repository.reporterepository;
import com.example.gestionreportes.webclient.usuarioclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import  com.example.gestionreportes.Service.reporteservice;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class reporteserviceTest {

    @Mock
    private reporterepository reporteRepository;

    @Mock
    private usuarioclient usuarioClient;

    @InjectMocks
    private reporteservice reporteservice;

    private reporte reporteEjemplo;

    @BeforeEach
    public void setUp() {
        reporteEjemplo = new reporte();
        reporteEjemplo.setId(1L);
        reporteEjemplo.setTiporeporte("Tipo1");
        reporteEjemplo.setDescripciongeneral("Descripcion de prueba");
        reporteEjemplo.setIdusuario(100L);
        reporteEjemplo.setFechareporte(new Date());
    }

    @Test
    public void testBuscarReportes() {
        List<reporte> lista = Arrays.asList(reporteEjemplo);
        when(reporteRepository.findAll()).thenReturn(lista);

        List<reporte> resultado = reporteservice.buscarReportes();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(reporteRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarReportePorId_existente() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporteEjemplo));

        reporte resultado = reporteservice.buscarreporteporid(1L);

        assertNotNull(resultado);
        assertEquals("Tipo1", resultado.getTiporeporte());
        verify(reporteRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarReportePorId_noExiste() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reporteservice.buscarreporteporid(1L);
        });

        assertEquals("reporte no encontrado", exception.getMessage());
        verify(reporteRepository, times(1)).findById(1L);
    }

    @Test
    public void testCrearReporte_usuarioExiste() {
        Map<String, Object> usuarioMock = new HashMap<>();
        usuarioMock.put("id", 100L);
        usuarioMock.put("nombre", "Usuario de prueba");

        when(usuarioClient.obtenerusuarioid(100L)).thenReturn(usuarioMock);
        when(reporteRepository.save(any(reporte.class))).thenReturn(reporteEjemplo);

        reporte resultado = reporteservice.crearReporte("Tipo1", "Descripcion de prueba", 100L);

        assertNotNull(resultado);
        assertEquals("Tipo1", resultado.getTiporeporte());
        verify(usuarioClient, times(1)).obtenerusuarioid(100L);
        verify(reporteRepository, times(1)).save(any(reporte.class));
    }

    @Test
    public void testCrearReporte_usuarioNoExiste() {
        when(usuarioClient.obtenerusuarioid(100L)).thenReturn(Collections.emptyMap());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reporteservice.crearReporte("Tipo1", "Descripcion de prueba", 100L);
        });

        assertEquals("Usuario no encontrado, no se puede agregar el reporte", exception.getMessage());
        verify(usuarioClient, times(1)).obtenerusuarioid(100L);
        verify(reporteRepository, never()).save(any());
    }

    @Test
    public void testEliminarReporte_existe() {
        when(reporteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reporteRepository).deleteById(1L);

        String mensaje = reporteservice.eliminarreporte(1L);

        assertEquals("El reporte se ha eliminado exitosamente", mensaje);
        verify(reporteRepository, times(1)).existsById(1L);
        verify(reporteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testEliminarReporte_noExiste() {
        when(reporteRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reporteservice.eliminarreporte(1L);
        });

        assertEquals("El reporte con ID: 1 no existe", exception.getMessage());
        verify(reporteRepository, times(1)).existsById(1L);
        verify(reporteRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testBuscarPorIdUsuario_conReportes() {
        List<reporte> lista = Arrays.asList(reporteEjemplo);
        when(reporteRepository.findByIdusuario(100L)).thenReturn(lista);

        List<reporte> resultado = reporteservice.buscarporidusuario(100L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(reporteRepository, times(1)).findByIdusuario(100L);
    }

    @Test
    public void testBuscarPorIdUsuario_sinReportes() {
        when(reporteRepository.findByIdusuario(100L)).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reporteservice.buscarporidusuario(100L);
        });

        assertEquals("No se encontraron equipos para el usuario con ID: 100", exception.getMessage());
        verify(reporteRepository, times(1)).findByIdusuario(100L);
    }
}
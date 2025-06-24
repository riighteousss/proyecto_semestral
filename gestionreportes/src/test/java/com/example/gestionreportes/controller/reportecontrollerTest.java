package com.example.gestionreportes.controller;

import com.example.gestionreportes.Controller.reportecontroller;
import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Service.reporteservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(reportecontroller.class)
@ExtendWith(MockitoExtension.class)
public class reportecontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private reporteservice reporteservice;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testObtenerTodosLosReportes_conReportes() throws Exception {
        reporte r1 = new reporte(1L, "ayuda", "el sitio no responde", 10L, null);
        reporte r2 = new reporte(2L, "error", "no puedo ingresar mi equipo al registro", 20L, null);

        List<reporte> listaMock = Arrays.asList(r1, r2);

        when(reporteservice.buscarReportes()).thenReturn(listaMock);

        mockMvc.perform(get("/api/v1/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(reporteservice, times(1)).buscarReportes();
    }

    @Test
    public void testObtenerTodosLosReportes_sinReportes() throws Exception {
        when(reporteservice.buscarReportes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/reportes"))
                .andExpect(status().isNoContent());

        verify(reporteservice, times(1)).buscarReportes();
    }

    @Test
    public void testCrearReporte_exitoso() throws Exception {
        reporte request = new reporte(2L, "TipoNuevo", "Descripcion Nueva", 1L, null);
        reporte saved = new reporte(1L, "TipoNuevo", "Descripcion Nueva", 2L, null);

        when(reporteservice.crearReporte(anyString(), anyString(), anyLong())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tiporeporte").value("TipoNuevo"));

        verify(reporteservice, times(1)).crearReporte(anyString(), anyString(), anyLong());
    }

    @Test
    public void testCrearReporte_error() throws Exception {
        reporte request = new reporte(3L, "TipoNuevo", "Descripcion Nueva", 3L, null);

        when(reporteservice.crearReporte(anyString(), anyString(), anyLong()))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(post("/api/v1/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado"));

        verify(reporteservice, times(1)).crearReporte(anyString(), anyString(), anyLong());
    }

    @Test
    public void testEliminarReporte_existente() throws Exception {
        when(reporteservice.eliminarreporte(1L))
                .thenReturn("Reporte eliminado correctamente");

        mockMvc.perform(delete("/api/v1/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reporte eliminado correctamente"));

        verify(reporteservice, times(1)).eliminarreporte(1L);
    }

    @Test
    public void testEliminarReporte_noExiste() throws Exception {
        when(reporteservice.eliminarreporte(1L))
                .thenThrow(new RuntimeException("Reporte no encontrado"));

        mockMvc.perform(delete("/api/v1/reportes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reporte no encontrado"));

        verify(reporteservice, times(1)).eliminarreporte(1L);
    }

    @Test
    public void testBuscarReportePorId_existente() throws Exception {
        reporte reporteMock = new reporte(1L, "Tipo1", "Descripcion1", 10L, null);

        when(reporteservice.buscarreporteporid(1L)).thenReturn(reporteMock);

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tiporeporte").value("Tipo1"));

        verify(reporteservice, times(1)).buscarreporteporid(1L);
    }

    @Test
    public void testBuscarReportePorId_noExiste() throws Exception {
        when(reporteservice.buscarreporteporid(1L))
                .thenThrow(new RuntimeException("Reporte no encontrado"));

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isNotFound());

        verify(reporteservice, times(1)).buscarreporteporid(1L);
    }

    @Test
    public void testBuscarReportesPorUsuario_conReportes() throws Exception {
        reporte r1 = new reporte(1L, "Tipo1", "Descripcion1", 10L, null);

        List<reporte> listaMock = Arrays.asList(r1);

        when(reporteservice.buscarporidusuario(10L)).thenReturn(listaMock);

        mockMvc.perform(get("/api/v1/reportes/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(reporteservice, times(1)).buscarporidusuario(10L);
    }

    @Test
    public void testBuscarReportesPorUsuario_noReportes() throws Exception {
        when(reporteservice.buscarporidusuario(10L))
                .thenThrow(new RuntimeException("No se encontraron reportes para el usuario con ID: 10"));

        mockMvc.perform(get("/api/v1/reportes/usuario/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron reportes para el usuario con ID: 10"));

        verify(reporteservice, times(1)).buscarporidusuario(10L);
    }
}

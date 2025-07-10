package com.example.equipos.controller;

import com.example.equipos.model.equipo;
import com.example.equipos.service.EquipoService;

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

@WebMvcTest(EquipoController.class)
@ExtendWith(MockitoExtension.class)
public class equipocontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipoService equipoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testObtenerTodosLosEquipos_conEquipos() throws Exception {
        equipo equipo1 = new equipo(1L, 1L, "Laptop", "Dell", "XPS");
        equipo equipo2 = new equipo(2L, 2L, "Tablet", "Samsung", "Tab S6");

        List<equipo> listaMock = Arrays.asList(equipo1, equipo2);

        when(equipoService.buscarquipos()).thenReturn(listaMock);

        mockMvc.perform(get("/api/v1/equipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(equipoService, times(1)).buscarquipos();
    }

    @Test
    public void testObtenerTodosLosEquipos_sinEquipos() throws Exception {
        when(equipoService.buscarquipos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/equipos"))
                .andExpect(status().isNoContent());

        verify(equipoService, times(1)).buscarquipos();
    }

    @Test
    public void testAgregarEquipo_usuarioEncontrado() throws Exception {
        equipo equipoNuevo = new equipo(1L, 1L, "Laptop", "HP", "Elitebook");

        when(equipoService.agregarequipo(anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(equipoNuevo);

        mockMvc.perform(post("/api/v1/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipoNuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipodispositivo").value("Laptop"));

        verify(equipoService, times(1)).agregarequipo(anyLong(), anyString(), anyString(), anyString(),anyString());
    }

    @Test
    public void testAgregarEquipo_usuarioNoEncontrado() throws Exception {
        equipo equipoNuevo = new equipo(1L, 1L, "Laptop", "HP", "Elitebook");

        when(equipoService.agregarequipo(anyLong(), anyString(), anyString(), anyString(),anyString()))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(post("/api/v1/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipoNuevo)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado"));
    }

    @Test
    public void testEliminarEquipo_existente() throws Exception {
        when(equipoService.eliminarequipoporid(1L))
                .thenReturn("el equipo de id:1 se ha eliminado correctamente");

        mockMvc.perform(delete("/api/v1/equipos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("el equipo de id:1 se ha eliminado correctamente"));

        verify(equipoService, times(1)).eliminarequipoporid(1L);
    }

    @Test
    public void testEliminarEquipo_noExiste() throws Exception {
        when(equipoService.eliminarequipoporid(1L))
                .thenThrow(new RuntimeException("el equipo de id:1 no existe"));

        mockMvc.perform(delete("/api/v1/equipos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("el equipo de id:1 no existe"));
    }

    @Test
    public void testBuscarEquiposPorUsuario_conEquipos() throws Exception {
        equipo equipo1 = new equipo(1L, 1L, "Laptop", "Dell", "XPS");

        List<equipo> listaMock = Arrays.asList(equipo1);

        when(equipoService.buscarporidusuario(1L)).thenReturn(listaMock);

        mockMvc.perform(get("/api/v1/equipos/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(equipoService, times(1)).buscarporidusuario(1L);
    }

    @Test
    public void testBuscarEquiposPorUsuario_noEquipos() throws Exception {
        when(equipoService.buscarporidusuario(1L))
                .thenThrow(new RuntimeException("No se encontraron equipos para el usuario con ID: 1"));

        mockMvc.perform(get("/api/v1/equipos/todos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontraron equipos para el usuario con ID: 1"));
    }

    @Test
    public void testObtenerEquipoPorId_existente() throws Exception {
        equipo equipoMock = new equipo(1L, 1L, "Laptop", "Dell", "XPS");

        when(equipoService.buscarporid(1L)).thenReturn(equipoMock);

        mockMvc.perform(get("/api/v1/equipos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testObtenerEquipoPorId_noExiste() throws Exception {
        when(equipoService.buscarporid(1L))
                .thenThrow(new RuntimeException("Equipo no encontrado"));

        mockMvc.perform(get("/api/v1/equipos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipo no encontrado"));
    }
}
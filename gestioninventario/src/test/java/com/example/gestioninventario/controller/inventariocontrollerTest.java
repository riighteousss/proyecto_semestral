package com.example.gestioninventario.controller;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@ExtendWith(MockitoExtension.class)
public class inventariocontrollerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testObtenerProductos_conResultados() throws Exception {
        inventario i = new inventario();
        i.setNombre("Mouse");

        when(inventarioService.buscarinventarios()).thenReturn(List.of(i));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(inventarioService, times(1)).buscarinventarios();
    }

    @Test
    public void testObtenerProductos_sinResultados() throws Exception {
        when(inventarioService.buscarinventarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isNoContent());

        verify(inventarioService, times(1)).buscarinventarios();
    }

    @Test
    public void testObtenerProductoPorId_existente() throws Exception {
        inventario i = new inventario();
        i.setId(1L);
        i.setNombre("Teclado");

        when(inventarioService.buscarPorId(1L)).thenReturn(java.util.Optional.of(i));

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testObtenerProductoPorId_noExiste() throws Exception {
        when(inventarioService.buscarPorId(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearProducto_exito() throws Exception {
        inventario nuevo = new inventario();
        nuevo.setNombre("Monitor");

        when(inventarioService.agregarinventario(any(), any(), any(), any(), any(), any(), any())).thenReturn(nuevo);

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Monitor"));

        verify(inventarioService, times(1)).agregarinventario(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testCrearProducto_error() throws Exception {
        inventario nuevo = new inventario();
        nuevo.setNombre("Monitor");

        when(inventarioService.agregarinventario(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Producto duplicado"));

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Producto duplicado"));
    }

    @Test
    public void testEliminarProducto_exito() throws Exception {
        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isOk());

        verify(inventarioService).eliminarProducto(1L);
    }

    @Test
    public void testEliminarProducto_error() throws Exception {
        doThrow(new RuntimeException("No encontrado")).when(inventarioService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No encontrado"));
    }

    @Test
    public void testActualizarStock_exito() throws Exception {
        inventario actualizado = new inventario();
        actualizado.setStock(10);

        when(inventarioService.actualizarStock(1L, 10)).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/productos/1/stock?stock=10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testReducirStock_error() throws Exception {
        when(inventarioService.reducirStock(1L, 50)).thenThrow(new RuntimeException("Stock insuficiente"));

        mockMvc.perform(put("/api/v1/productos/1/stock/reducir?cantidad=50"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Stock insuficiente"));
    }

    @Test
    public void testReactivarProducto_exito() throws Exception {
        inventario producto = new inventario();
        producto.setActivo(true);

        when(inventarioService.reactivarProducto(1L)).thenReturn(producto);

        mockMvc.perform(put("/api/v1/productos/1/reactivar"))
                .andExpect(status().isOk());

        verify(inventarioService).reactivarProducto(1L);
    }
}

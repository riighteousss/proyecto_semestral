package com.example.gestioninventario.service;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.repository.Repositoryinventario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class inventarioserviceTest {

    @Mock
    private Repositoryinventario repositoryinventario;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    public void testBuscarInventarios_Activos() {
        List<inventario> lista = List.of(new inventario());
        when(repositoryinventario.findByActivoTrue()).thenReturn(lista);

        List<inventario> resultado = inventarioService.buscarinventarios();

        assertEquals(1, resultado.size());
        verify(repositoryinventario).findByActivoTrue();
    }

    @Test
    public void testBuscarTodos() {
        List<inventario> lista = List.of(new inventario(), new inventario());
        when(repositoryinventario.findAll()).thenReturn(lista);

        List<inventario> resultado = inventarioService.buscarTodos();

        assertEquals(2, resultado.size());
        verify(repositoryinventario).findAll();
    }

    @Test
    public void testBuscarPorId_Encontrado() {
        inventario mock = new inventario();
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(mock));

        Optional<inventario> resultado = inventarioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        verify(repositoryinventario).findById(1L);
    }

    @Test
    public void testBuscarPorNombre() {
        List<inventario> lista = List.of(new inventario());
        when(repositoryinventario.findByNombreContainingIgnoreCase("tornillo")).thenReturn(lista);

        List<inventario> resultado = inventarioService.buscarPorNombre("tornillo");

        assertEquals(1, resultado.size());
        verify(repositoryinventario).findByNombreContainingIgnoreCase("tornillo");
    }

    @Test
    public void testBuscarPorCategoria() {
        List<inventario> lista = List.of(new inventario());
        when(repositoryinventario.findByCategoria("ferreteria")).thenReturn(lista);

        List<inventario> resultado = inventarioService.buscarPorCategoria("ferreteria");

        assertEquals(1, resultado.size());
        verify(repositoryinventario).findByCategoria("ferreteria");
    }

    @Test
    public void testBuscarStockBajo() {
        List<inventario> lista = List.of(new inventario());
        when(repositoryinventario.findByStockLessThanEqualStockMinimo()).thenReturn(lista);

        List<inventario> resultado = inventarioService.buscarStockBajo();

        assertEquals(1, resultado.size());
        verify(repositoryinventario).findByStockLessThanEqualStockMinimo();
    }

    @Test
    public void testAgregarInventario_Exito() {
        when(repositoryinventario.findByNombreContainingIgnoreCase("Nuevo Producto")).thenReturn(Collections.emptyList());
        when(repositoryinventario.save(any())).thenAnswer(inv -> inv.getArgument(0));

        inventario resultado = inventarioService.agregarinventario("Nuevo Producto", "desc", 10, 2, "cat", "unidad", 1000.0);

        assertEquals("Nuevo Producto", resultado.getNombre());
        assertEquals(10, resultado.getStock());
        verify(repositoryinventario).save(any());
    }

    @Test
    public void testAgregarInventario_NombreVacio() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            inventarioService.agregarinventario(" ", "desc", 10, 2, "cat", "unidad", 1000.0);
        });
        assertTrue(ex.getMessage().contains("obligatorio"));
    }

    @Test
    public void testAgregarInventario_NombreRepetido() {
        when(repositoryinventario.findByNombreContainingIgnoreCase("Duplicado")).thenReturn(List.of(new inventario()));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            inventarioService.agregarinventario("Duplicado", "desc", 10, 2, "cat", "unidad", 1000.0);
        });
        assertTrue(ex.getMessage().contains("Ya existe"));
    }

    @Test
    public void testModificarProducto_Exito() {
        inventario original = new inventario(1L, "Original", "", 10, 2, "", "", 1000.0, true);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(original));
        when(repositoryinventario.findByNombreContainingIgnoreCase("Modificado")).thenReturn(Collections.emptyList());
        when(repositoryinventario.save(any())).thenAnswer(inv -> inv.getArgument(0));

        inventario resultado = inventarioService.modificarProducto(1L, "Modificado", "nueva desc", 20, 5, "cat", "unidad", 2000.0);

        assertEquals("Modificado", resultado.getNombre());
        assertEquals(20, resultado.getStock());
    }

    @Test
    public void testActualizarStock() {
        inventario inv = new inventario();
        inv.setId(1L);
        inv.setStock(5);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));
        when(repositoryinventario.save(any())).thenAnswer(invoc -> invoc.getArgument(0));

        inventario actualizado = inventarioService.actualizarStock(1L, 15);

        assertEquals(15, actualizado.getStock());
    }

    @Test
    public void testActualizarStock_Negativo() {
        inventario inv = new inventario();
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            inventarioService.actualizarStock(1L, -5);
        });

        assertTrue(ex.getMessage().contains("negativo"));
    }

    @Test
    public void testAgregarStock() {
        inventario inv = new inventario();
        inv.setStock(10);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));
        when(repositoryinventario.save(any())).thenAnswer(i -> i.getArgument(0));

        inventario resultado = inventarioService.agregarStock(1L, 5);

        assertEquals(15, resultado.getStock());
    }

    @Test
    public void testReducirStock_Exito() {
        inventario inv = new inventario();
        inv.setStock(10);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));
        when(repositoryinventario.save(any())).thenAnswer(i -> i.getArgument(0));

        inventario resultado = inventarioService.reducirStock(1L, 5);

        assertEquals(5, resultado.getStock());
    }

    @Test
    public void testReducirStock_Insuficiente() {
        inventario inv = new inventario();
        inv.setStock(4);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            inventarioService.reducirStock(1L, 10);
        });

        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    public void testEliminarProducto() {
        inventario inv = new inventario();
        inv.setActivo(true);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));

        inventarioService.eliminarProducto(1L);

        assertFalse(inv.getActivo());
        verify(repositoryinventario).save(inv);
    }

    @Test
    public void testReactivarProducto() {
        inventario inv = new inventario();
        inv.setActivo(false);
        when(repositoryinventario.findById(1L)).thenReturn(Optional.of(inv));
        when(repositoryinventario.save(any())).thenAnswer(i -> i.getArgument(0));

        inventario reactivado = inventarioService.reactivarProducto(1L);

        assertTrue(reactivado.getActivo());
    }
}

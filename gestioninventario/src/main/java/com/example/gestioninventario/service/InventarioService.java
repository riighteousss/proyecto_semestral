package com.example.gestioninventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.repository.Repositoryinventario;

@Service
public class InventarioService {
    @Autowired
    private Repositoryinventario repositoryinventario;
    
    // Buscar todos los productos activos
    public List<inventario> buscarinventarios() {
        return repositoryinventario.findByActivoTrue();
    }
    
    // Buscar todos los productos (incluyendo inactivos)
    public List<inventario> buscarTodos() {
        return repositoryinventario.findAll();
    }
    
    // Buscar por ID
    public Optional<inventario> buscarPorId(Long id) {
        return repositoryinventario.findById(id);
    }
    
    // Buscar por nombre
    public List<inventario> buscarPorNombre(String nombre) {
        return repositoryinventario.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar por categoría
    public List<inventario> buscarPorCategoria(String categoria) {
        return repositoryinventario.findByCategoria(categoria);
    }
    
    // Buscar productos con stock bajo
    public List<inventario> buscarStockBajo() {
        return repositoryinventario.findByStockLessThanEqualStockMinimo();
    }

    // Crear nuevo producto
    public inventario agregarinventario(String nombre, String descripcion, Integer stock, 
                                      Integer stockMinimo, String categoria, String unidadMedida, Double precio) {
        // Validar que el nombre no esté vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        
        // Verificar si ya existe un producto con el mismo nombre
        List<inventario> productosExistentes = repositoryinventario.findByNombreContainingIgnoreCase(nombre.trim());
        if (!productosExistentes.isEmpty()) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + nombre);
        }
        
        inventario inventario = new inventario();
        inventario.setNombre(nombre.trim());
        inventario.setDescripcion(descripcion);
        inventario.setStock(stock != null ? stock : 0);
        inventario.setStockMinimo(stockMinimo != null ? stockMinimo : 0);
        inventario.setCategoria(categoria);
        inventario.setUnidadMedida(unidadMedida);
        inventario.setPrecio(precio);
        inventario.setActivo(true);
        
        return repositoryinventario.save(inventario);
    }
    
    // Modificar producto existente
    public inventario modificarProducto(Long id, String nombre, String descripcion, Integer stock, 
                                      Integer stockMinimo, String categoria, String unidadMedida, Double precio) {
        inventario inventario = repositoryinventario.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        // Verificar si el nuevo nombre ya existe en otro producto
        if (nombre != null && !nombre.trim().isEmpty() && !nombre.equals(inventario.getNombre())) {
            List<inventario> productosExistentes = repositoryinventario.findByNombreContainingIgnoreCase(nombre.trim());
            if (!productosExistentes.isEmpty() && !productosExistentes.get(0).getId().equals(id)) {
                throw new RuntimeException("Ya existe otro producto con el nombre: " + nombre);
            }
        }
        
        // Actualizar campos
        if (nombre != null && !nombre.trim().isEmpty()) inventario.setNombre(nombre.trim());
        if (descripcion != null) inventario.setDescripcion(descripcion);
        if (stock != null) inventario.setStock(stock);
        if (stockMinimo != null) inventario.setStockMinimo(stockMinimo);
        if (categoria != null) inventario.setCategoria(categoria);
        if (unidadMedida != null) inventario.setUnidadMedida(unidadMedida);
        if (precio != null) inventario.setPrecio(precio);
        
        return repositoryinventario.save(inventario);
    }
    
    // Actualizar solo el stock
    public inventario actualizarStock(Long id, Integer nuevoStock) {
        inventario inventario = repositoryinventario.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        
        inventario.setStock(nuevoStock);
        return repositoryinventario.save(inventario);
    }
    
    // Agregar stock
    public inventario agregarStock(Long id, Integer cantidad) {
        inventario inventario = repositoryinventario.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad a agregar debe ser positiva");
        }
        
        inventario.setStock(inventario.getStock() + cantidad);
        return repositoryinventario.save(inventario);
    }
    
    // Reducir stock
    public inventario reducirStock(Long id, Integer cantidad) {
        inventario inventario = repositoryinventario.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad a reducir debe ser positiva");
        }
        
        if (inventario.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + inventario.getStock());
        }
        
        inventario.setStock(inventario.getStock() - cantidad);
        return repositoryinventario.save(inventario);
    }
    
    // Eliminar producto (desactivar)
    public void eliminarProducto(Long id) {
        inventario inventario = repositoryinventario.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        inventario.setActivo(false);
        repositoryinventario.save(inventario);
    }
    
    // Reactivar producto
    public inventario reactivarProducto(Long id) {
        inventario inventario = repositoryinventario.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        inventario.setActivo(true);
        return repositoryinventario.save(inventario);
    }
}
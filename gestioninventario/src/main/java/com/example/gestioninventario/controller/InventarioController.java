package com.example.gestioninventario.controller;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class InventarioController {
    
    @Autowired
    private InventarioService inventarioService;
    
    // Obtener todos los productos activos
    @GetMapping("/productos")
    public ResponseEntity<List<inventario>> obtenerProductos() {
        List<inventario> productos = inventarioService.buscarinventarios();
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener todos los productos (incluyendo inactivos)
    @GetMapping("/productos/todos")
    public ResponseEntity<List<inventario>> obtenerTodosLosProductos() {
        List<inventario> productos = inventarioService.buscarTodos();
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener producto por ID
    @GetMapping("/productos/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Optional<inventario> producto = inventarioService.buscarPorId(id);
            return producto.isPresent() ? 
                ResponseEntity.ok(producto.get()) : 
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    // Obtener productos por nombre
    @GetMapping("/productos/nombre/{nombre}")
    public ResponseEntity<List<inventario>> obtenerProductosPorNombre(@PathVariable String nombre) {
        List<inventario> productos = inventarioService.buscarPorNombre(nombre);
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener productos por categoría
    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<List<inventario>> obtenerProductosPorCategoria(@PathVariable String categoria) {
        List<inventario> productos = inventarioService.buscarPorCategoria(categoria);
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener productos con stock bajo
    @GetMapping("/productos/stock-bajo")
    public ResponseEntity<List<inventario>> obtenerProductosStockBajo() {
        List<inventario> productos = inventarioService.buscarStockBajo();
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Crear nuevo producto
    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody inventario producto) {
        try {
            inventario nuevoProducto = inventarioService.agregarinventario(
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getStockMinimo(),
                producto.getCategoria(),
                producto.getUnidadMedida(),
                producto.getPrecio()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Modificar producto completo
    @PutMapping("/productos/{id}")
    public ResponseEntity<?> modificarProducto(
            @PathVariable Long id,
            @RequestBody inventario productoData) {
        try {
            inventario productoActualizado = inventarioService.modificarProducto(
                id,
                productoData.getNombre(),
                productoData.getDescripcion(),
                productoData.getStock(),
                productoData.getStockMinimo(),
                productoData.getCategoria(),
                productoData.getUnidadMedida(),
                productoData.getPrecio()
            );
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Actualizar solo el stock
    @PutMapping("/productos/{id}/stock")
    public ResponseEntity<?> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        try {
            inventario productoActualizado = inventarioService.actualizarStock(id, stock);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Agregar stock
    @PutMapping("/productos/{id}/stock/agregar")
    public ResponseEntity<?> agregarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        try {
            inventario productoActualizado = inventarioService.agregarStock(id, cantidad);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Reducir stock
    @PutMapping("/productos/{id}/stock/reducir")
    public ResponseEntity<?> reducirStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        try {
            inventario productoActualizado = inventarioService.reducirStock(id, cantidad);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Reactivar producto
    @PutMapping("/productos/{id}/reactivar")
    public ResponseEntity<?> reactivarProducto(@PathVariable Long id) {
        try {
            inventario productoReactivado = inventarioService.reactivarProducto(id);
            return ResponseEntity.ok(productoReactivado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Eliminar producto (desactivar)
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            inventarioService.eliminarProducto(id);
            return ResponseEntity.ok().body("Producto eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
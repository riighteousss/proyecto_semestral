package com.example.gestioninventario.controller;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "Obtener productos activos", description = "Devuelve una lista con todos los productos activos en inventario")
          @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "204", description = "No hay productos activos")
})
    @GetMapping("/productos")
    public ResponseEntity<List<inventario>> obtenerProductos() {
        List<inventario> productos = inventarioService.buscarinventarios();
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener todos los productos (incluyendo inactivos)
    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista con todos los productos, incluyendo los inactivos")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "204", description = "No hay productos registrados")
})
    @GetMapping("/productos/todos")
    public ResponseEntity<List<inventario>> obtenerTodosLosProductos() {
        List<inventario> productos = inventarioService.buscarTodos();
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener producto por ID
       @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto por su ID")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto obtenido correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
  })
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
      @Operation(summary = "Obtener productos por nombre", description = "Devuelve una lista de productos que coinciden con el nombre")
      @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "204", description = "No se encontraron productos con ese nombre")
})
    @GetMapping("/productos/nombre/{nombre}")
    public ResponseEntity<List<inventario>> obtenerProductosPorNombre(@PathVariable String nombre) {
        List<inventario> productos = inventarioService.buscarPorNombre(nombre);
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener productos por categoría
    @Operation(summary = "Obtener productos por categoría", description = "Devuelve una lista de productos que pertenecen a la categoría")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "204", description = "No se encontraron productos en esa categoria")
})
    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<List<inventario>> obtenerProductosPorCategoria(@PathVariable String categoria) {
        List<inventario> productos = inventarioService.buscarPorCategoria(categoria);
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Obtener productos con stock bajo
    @Operation(summary = "Obtener productos con stock bajo", description = "Devuelve una lista de productos los cuales su stock es menor al mínimo")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Productos con stock bajo obtenidos correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "204", description = "No hay productos con stock bajo")
})
    @GetMapping("/productos/stock-bajo")
    public ResponseEntity<List<inventario>> obtenerProductosStockBajo() {
        List<inventario> productos = inventarioService.buscarStockBajo();
        return productos.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(productos);
    }
    
    // Crear nuevo producto
    @Operation(summary = "Crear nuevo producto", description = "Permite registrar un nuevo producto en el inventario")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})
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
    @Operation(summary = "Modificar producto", description = "Permite modificar completamente los datos de un producto existente")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto modificado correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})
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
    @Operation(summary = "Actualizar stock de producto", description = "Permite actualizar del stock de un producto")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})

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
    @Operation(summary = "Agregar stock a un producto", description = "Permite agregar unidades al stock de un producto")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock agregado correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})
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
    @Operation(summary = "Reducir stock de un producto", description = "Permite reducir stock de un producto")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Stock reducido correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})
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
    @Operation(summary = "Reactivar producto", description = "Permite reactivar un producto inactivado")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto reactivado correctamente",
        content = @Content(schema = @Schema(implementation = inventario.class))),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})
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
     @Operation(summary = "Eliminar producto (desactivar)", description = "Permite eliminar (desactivar) un producto del inventario")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
    @ApiResponse(responseCode = "400", description = "Error en la solicitud. Datos inválidos.")
})

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
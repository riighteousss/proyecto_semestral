package com.example.gestioninventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.gestioninventario.model.inventario;

import java.util.List;

@Repository
public interface Repositoryinventario extends JpaRepository<inventario, Long> {
    
    // Buscar productos activos
    List<inventario> findByActivoTrue();
    
    // Buscar por nombre (case insensitive)
    List<inventario> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por categoria
    List<inventario> findByCategoria(String categoria);
    
    // Buscar productos con stock bajo (stock <= stockMinimo)
    @Query("SELECT i FROM inventario i WHERE i.stock <= i.stockMinimo AND i.activo = true")
    List<inventario> findByStockLessThanEqualStockMinimo();
    
    // Buscar productos por rango de stock
    List<inventario> findByStockBetween(Integer stockMin, Integer stockMax);
    
    // Buscar productos activos por categoria
    List<inventario> findByCategoriaAndActivoTrue(String categoria);
}
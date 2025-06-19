package com.example.gestionfacturacion.repository;

import com.example.gestionfacturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
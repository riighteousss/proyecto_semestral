package com.example.gestioninventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestioninventario.model.inventario;

@Repository
public interface Repositoryinventario extends JpaRepository<inventario, Long>  {

}
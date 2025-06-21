package com.example.gestiontecnicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestiontecnicos.model.tecnicos;

@Repository
public interface RepositoryTecnicos extends JpaRepository<tecnicos, Long>{
    Optional<tecnicos> findByRut(String rut); 
}

package com.example.gestionsolicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionsolicitudes.model.solicitud;

@Repository
public interface solicitudrepository extends JpaRepository<solicitud, Long>{

}

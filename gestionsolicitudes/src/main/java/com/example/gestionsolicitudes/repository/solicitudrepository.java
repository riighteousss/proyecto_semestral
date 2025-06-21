package com.example.gestionsolicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionsolicitudes.model.solicitud;
import java.util.List;


@Repository
public interface solicitudrepository extends JpaRepository<solicitud, Long>{
 List<solicitud> findByIdusuario(Long idusuario);
}

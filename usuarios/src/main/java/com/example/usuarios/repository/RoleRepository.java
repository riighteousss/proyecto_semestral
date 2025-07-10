package com.example.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.usuarios.model.Rol;

@Repository
public interface RoleRepository extends JpaRepository<Rol, Long>{

 Optional<Rol> findByNombre(String nombre);

}

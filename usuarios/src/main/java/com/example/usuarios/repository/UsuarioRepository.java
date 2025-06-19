package com.example.usuarios.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.usuarios.model.usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<usuario, Long> {
    
    // Método existente para verificar username único
    boolean existsByUsername(String username);
    
    // Método existente para verificar correo único
    boolean existsByCorreo(String correo);
    
    // --- NUEVO MÉTODO PARA LOGIN ---
    // Busca un usuario por su username (retorna Optional para manejar nulls)
    Optional<usuario> findByUsername(String username);
}
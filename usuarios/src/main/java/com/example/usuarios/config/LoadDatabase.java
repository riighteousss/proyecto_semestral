package com.example.usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.usuarios.model.Rol;
import com.example.usuarios.model.usuario;
import com.example.usuarios.repository.RoleRepository;
import com.example.usuarios.repository.UsuarioRepository;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepo, UsuarioRepository userRepo) {
        return args -> {
            
            if (roleRepo.count() == 0 && userRepo.count() == 0) {
                // Insertar roles
                Rol admin = new Rol();
                admin.setNombre("Administrador");
                roleRepo.save(admin);

                Rol user = new Rol();
                user.setNombre("Usuario");
                roleRepo.save(user);

                // Insertar usuarios
                usuario u1 = new usuario();
                u1.setUsername("admin");
                u1.setPassword("admin123"); // En producción deberías cifrar esto
                u1.setCorreo("admin@example.com");
                u1.setRol(admin);
                userRepo.save(u1);

                usuario u2 = new usuario();
                u2.setUsername("juan");
                u2.setPassword("juan123");
                u2.setCorreo("juan@example.com");
                u2.setRol(user);
                userRepo.save(u2);
            }
        };
    }
}
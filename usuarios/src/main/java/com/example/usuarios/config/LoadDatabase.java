package com.example.usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.usuarios.model.Rol;
import com.example.usuarios.repository.RoleRepository;
import com.example.usuarios.service.UsuarioService;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepo, UsuarioService usuarioService) {
        return args -> {
            if (roleRepo.count() == 0) {
                // Crear roles
                Rol admin = new Rol();
                admin.setNombre("ADMIN");
                roleRepo.save(admin);

                Rol user = new Rol();
                user.setNombre("USER");
                roleRepo.save(user);
            }

            if (usuarioService.buscarUsuarios().isEmpty()) {
                // Obtener IDs de roles
                Long adminRoleId = roleRepo.findByNombre("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"))
                        .getId();
                Long userRoleId = roleRepo.findByNombre("USER")
                        .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"))
                        .getId();

                // Crear usuarios usando el servicio para que se maneje todo (contraseña cifrada, validaciones, etc)
                usuarioService.crearUsuario("admin", "admin123", "admin@example.com", adminRoleId);
                usuarioService.crearUsuario("juan", "juan123", "juan@example.com", userRoleId);
            }
        };
    }
}

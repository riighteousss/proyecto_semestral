package com.example.usuarios.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.usuarios.model.Rol;
import com.example.usuarios.repository.RoleRepository;
import com.example.usuarios.repository.UsuarioRepository;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepo, UsuarioRepository userRepo){
        return args ->{
            //si no hay registros en la tablas
            if(roleRepo.count() == 0 && userRepo.count() == 0){
                //insertar los roles por defecto
                Rol admin = new Rol();
                admin.setNombre("Administrador");
                roleRepo.save(admin);

                Rol usuario = new Rol();
                usuario.setNombre("Usuario");
                roleRepo.save(usuario);

                
            }
        };
    }
}
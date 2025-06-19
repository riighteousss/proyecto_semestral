package com.example.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usuarios.model.Rol;
import com.example.usuarios.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    //metodo para buscar todos los roles
    public List<Rol> buscarRoles(){
        return roleRepository.findAll();
    }

    //metodo para buscar un rol por su id
    public Rol obtenerRolPorId(Long id){
        return roleRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Rol no encontrado ID: " + id));
    }

}
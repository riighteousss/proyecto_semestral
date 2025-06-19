package com.example.gestioninventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.repository.Repositoryinventario;

@Service
public class serviceinventario {
    @Autowired
    private Repositoryinventario repositoryinventario;
    

    //metodo para buscar todos los usuarios
    public List<inventario> buscarinventarios(){
        return repositoryinventario.findAll();
    }

    //metodo para crear un nuevo usuario
    public inventario agregarinventario(String  nombre, String descripcion){
        
        inventario inventario = new inventario();
        inventario.setNombre(nombre);
        inventario.setDescripcion(descripcion);
        return repositoryinventario.save(inventario);

    }

}

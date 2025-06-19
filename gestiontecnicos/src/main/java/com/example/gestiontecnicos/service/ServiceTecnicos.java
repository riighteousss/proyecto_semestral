package com.example.gestiontecnicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestiontecnicos.model.tecnicos;
import com.example.gestiontecnicos.repository.RepositoryTecnicos;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServiceTecnicos {
    @Autowired
    private RepositoryTecnicos RepositoryTecnicos;

    //metodo para buscar todos los tecnicos
    public List<tecnicos> BuscarTecnicos(){
        return RepositoryTecnicos.findAll();
    }

    //agregar o crear un nuevo tecnico
    public tecnicos AgregarTecnico( String rut, String nombre, String especialidad){
       
       // Verificar si el técnico ya existe
        RepositoryTecnicos.findByRut(rut).ifPresent(t -> {
            throw new RuntimeException("El técnico con RUT " + rut + " ya existe.");
        });
        
        tecnicos tec= new tecnicos();
        tec.setRut(rut);
        tec.setNombre(nombre);
        tec.setEspecialidad(especialidad);
        return RepositoryTecnicos.save(tec);
        
    }
}

package com.example.gestiontecnicos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestiontecnicos.model.tecnicos;
import com.example.gestiontecnicos.repository.RepositoryTecnicos;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServiceTecnicos {
    @Autowired
    private RepositoryTecnicos repositoryTecnicos;

    // Método para buscar todos los técnicos
    public List<tecnicos> buscarTecnicos(){
        return repositoryTecnicos.findAll();
    }

    // Método para buscar técnico por ID
    public Optional<tecnicos> buscarPorId(Long id) {
        return repositoryTecnicos.findById(id);
    }

    // Método para buscar técnico por RUT
    public Optional<tecnicos> buscarPorRut(String rut) {
        return repositoryTecnicos.findByRut(rut);
    }

    // Agregar o crear un nuevo técnico
    public tecnicos agregarTecnico(String rut, String nombre, String especialidad){
        // Verificar si el técnico ya existe usando el método más eficiente
        if (repositoryTecnicos.existsByRut(rut)) {
            throw new RuntimeException("El técnico con RUT " + rut + " ya existe.");
        }
        
        tecnicos tec = new tecnicos();
        tec.setRut(rut);
        tec.setNombre(nombre);
        tec.setEspecialidad(especialidad);
        return repositoryTecnicos.save(tec);
    }

    // Modificar técnico existente
    public tecnicos modificarTecnico(Long id, String rut, String nombre, String especialidad) {
        Optional<tecnicos> tecnicoExistente = repositoryTecnicos.findById(id);
        
        if (!tecnicoExistente.isPresent()) {
            throw new RuntimeException("Técnico con ID " + id + " no encontrado.");
        }

        tecnicos tecnico = tecnicoExistente.get();
        
        // Verificar si el nuevo RUT ya existe en otro técnico
        if (!tecnico.getRut().equals(rut)) {
            if (repositoryTecnicos.existsByRutAndIdNot(rut, id)) {
                throw new RuntimeException("El RUT " + rut + " ya está asignado a otro técnico.");
            }
        }

        tecnico.setRut(rut);
        tecnico.setNombre(nombre);
        tecnico.setEspecialidad(especialidad);
        
        return repositoryTecnicos.save(tecnico);
    }

    // Eliminar técnico
    public void eliminarTecnico(Long id) {
        Optional<tecnicos> tecnicoExistente = repositoryTecnicos.findById(id);
        
        if (!tecnicoExistente.isPresent()) {
            throw new RuntimeException("Técnico con ID " + id + " no encontrado.");
        }

        repositoryTecnicos.deleteById(id);
    }

    // Actualizar solo la especialidad de un técnico
    public tecnicos actualizarEspecialidad(Long id, String especialidad) {
        Optional<tecnicos> tecnicoExistente = repositoryTecnicos.findById(id);
        
        if (!tecnicoExistente.isPresent()) {
            throw new RuntimeException("Técnico con ID " + id + " no encontrado.");
        }

        tecnicos tecnico = tecnicoExistente.get();
        tecnico.setEspecialidad(especialidad);
        
        return repositoryTecnicos.save(tecnico);
    }

    // Métodos de búsqueda adicionales usando el repository

    // Buscar técnicos por especialidad
    public List<tecnicos> buscarPorEspecialidad(String especialidad) {
        return repositoryTecnicos.findByEspecialidadOrderByNombre(especialidad);
    }

    // Buscar técnicos por nombre (búsqueda parcial)
    public List<tecnicos> buscarPorNombre(String nombre) {
        return repositoryTecnicos.findByNombreContainingIgnoreCase(nombre);
    }

    // Obtener todas las especialidades disponibles
    public List<String> obtenerEspecialidades() {
        return repositoryTecnicos.findDistinctEspecialidades();
    }

    // Contar técnicos por especialidad
    public long contarPorEspecialidad(String especialidad) {
        return repositoryTecnicos.countByEspecialidad(especialidad);
    }
}
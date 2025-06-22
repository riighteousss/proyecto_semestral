package com.example.equipos.service;
import com.example.equipos.model.equipo;
import com.example.equipos.repository.EquipoRepository;
import com.example.equipos.webclient.usuarioclient;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EquipoService {
     @Autowired
     private EquipoRepository equiporepository;
     @Autowired
    private usuarioclient usuarioClient;
    
    //metodo para ver todos los equipos
    public List<equipo> buscarquipos(){
      return equiporepository.findAll();
    }

    //metodo para buscar equipo por id 
    public equipo buscarporid(Long id){
        return equiporepository.findById(id)
        .orElseThrow(()-> new RuntimeException("equipo de ID:"+ id +"no encontrado"));
    }
    


    //metodo para buscar todos los equipos por idusuario
    public List<equipo> buscarporidusuario(Long idUsuario) {
          List<equipo> equipos = equiporepository.findByIdusuario(idUsuario);
        if (equipos.isEmpty()) {
              throw new RuntimeException("No se encontraron equipos para el usuario con ID: " + idUsuario);
           }
          return equipos;
    }


    //metodo para agregar un nuevo equipo

    public equipo agregarequipo(Long idusuario, String tipodispositivo, String marca, String modelo){

        //validar si el usuario existe antes de agregar el equipo
        Map<String, Object> usuario = usuarioClient.obtenerusuarioid(idusuario);
        
         if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado, no se puede agregar el reporte");
        }

        equipo newequipo = new equipo();
        newequipo.setIdusuario(idusuario);
        newequipo.setTipodispositivo(tipodispositivo);
        newequipo.setMarca(marca);
        newequipo.setModelo(modelo);

        return equiporepository.save(newequipo);

    }

    //metodo para eliminar un equipo
    public String eliminarequipoporid(Long id){
        //primero valida si existe 
        if(!equiporepository.existsById(id)){
             throw new RuntimeException("el equipo de id:"+ id + " no existe");
        }
        equiporepository.deleteById(id);

        return "el equipo de id:" + id + " se ha eliminado correctamente";
    }
  
}

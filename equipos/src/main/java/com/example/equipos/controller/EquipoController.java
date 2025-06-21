package com.example.equipos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.equipos.model.equipo;
import com.example.equipos.service.EquipoService;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;





@RestController
@RequestMapping("/api/v1")
public class EquipoController {
     @Autowired
     private EquipoService equiposervice;


    //endpoint para obtener de todos los equipos
    @GetMapping("/equipos")
    public ResponseEntity<List<equipo>> obtenertodoslosquipos() {
        List<equipo> Equipo = equiposervice.buscarquipos();
        //si la lista esta vacia
        if(Equipo.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Equipo);
    }

    //end point para agregar un equipo
    @PostMapping("/equipos")
    public ResponseEntity<?> agregarequipo(@RequestBody equipo newequipo) {
        try {

        equiposervice.agregarequipo(
            newequipo.getIdusuario(), 
            newequipo.getTipodispositivo(),
            newequipo.getMarca(),
            newequipo.getModelo()
            );

        return ResponseEntity.status(HttpStatus.CREATED).body(newequipo);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }
    @DeleteMapping("/equipos/{id}")
    public ResponseEntity<String> eliminarquipoporid(@PathVariable Long id){
        try {
        String mensaje = equiposervice.eliminarequipoporid(id);
        return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //enpoint para buscar todos los equipos de un usuario
    @GetMapping("/equipos/todos/{idusuario}")
    public ResponseEntity<?> buscarequiposdeusuarioporid(@PathVariable Long idusuario) {
        try {
         List<equipo> equipos = equiposervice.buscarporidusuario(idusuario);

        return ResponseEntity.ok(equipos);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
     @GetMapping("/equipos/{id}")
    public ResponseEntity<?> obtenerequiporid(@PathVariable Long id) {
           try {
            equipo equipo = equiposervice.buscarporid(id);
            return ResponseEntity.ok(equipo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
 
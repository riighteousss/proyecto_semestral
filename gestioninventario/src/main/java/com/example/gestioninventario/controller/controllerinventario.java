package com.example.gestioninventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestioninventario.model.inventario;
import com.example.gestioninventario.service.serviceinventario;

@RestController
@RequestMapping("/api/v1")
public class controllerinventario {
    @Autowired
    private serviceinventario inventarioservice;
    
    //endponit para consultar todos en el inventario
    @GetMapping("/inventario")
    public ResponseEntity<List<inventario>> obtenerinventarios(){
        List<inventario> inventario = inventarioservice.buscarinventarios();
        //si la lista esta vacia
        if(inventario.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventario);
    }

    //endpoint para agregar objeto a inventario
    @PostMapping("/inventario")
    public ResponseEntity<?> crearinventario(@RequestBody inventario inventario){
    try {
        inventario newinventario = inventarioservice.agregarinventario(
            inventario.getNombre(),
            inventario.getDescripcion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newinventario);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    }
}

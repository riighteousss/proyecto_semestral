package com.example.gestiontecnicos.controller;

import java.util.List;
import java.util.Map;

import com.example.gestiontecnicos.model.tecnicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestiontecnicos.service.ServiceTecnicos;



@RestController
@RequestMapping("/api/v1")
public class tecnicocontroller {
    @Autowired
    private ServiceTecnicos serviceTecnicos;

     //endponit para consultar todos los tecnicos
    @GetMapping("/tecnicos")
    public ResponseEntity<List<tecnicos>> obtenertecnicos(){
        List<tecnicos> tecnico = serviceTecnicos.BuscarTecnicos();
        //si la lista esta vacia
        if(tecnico.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tecnico);
    }
    //endpoint para buscar tecnico por id
    @GetMapping("/tecnicos/{id}")
    public ResponseEntity<?> buscarporid(@PathVariable Long id) {
        try {
            tecnicos tec = serviceTecnicos.buscarporid(id);

            return ResponseEntity.ok(tec);
    }   catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
    }
    

    //endpoint para crea un nuevo tecnico
    @PostMapping("/tecnicos")
    public ResponseEntity<?> creartecnico(@RequestBody tecnicos tecnico){
    try {
        tecnicos newtecnico = serviceTecnicos.AgregarTecnico(
            tecnico.getRut(),
            tecnico.getNombre(),
            tecnico.getEspecialidad()

        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newtecnico);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    }
    @PutMapping("/tecnicos/{id}/especialidad")
    public ResponseEntity<?> actualizarEspecialidad(@PathVariable Long id, @RequestBody Map<String, String> body) {
    try {
        String especialidad = body.get("especialidad");

       
        tecnicos tecnicoActualizado = serviceTecnicos.actualizarEspecializacion(id, especialidad);

       
        return ResponseEntity.ok(tecnicoActualizado);

    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

}

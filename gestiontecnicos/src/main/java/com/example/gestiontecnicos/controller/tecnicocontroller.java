package com.example.gestiontecnicos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "tecnicos",description = "operaciones relacionada con la gestion de tecnicos")
@RestController
@RequestMapping("/api/v1")
public class tecnicocontroller {
    @Autowired
    private ServiceTecnicos serviceTecnicos;

     //endponit para consultar todos los tecnicos
     @Operation(summary = "obtener todos los tecnicos existentes", description = "devuelve una lista con todos los tecnicos")
     @ApiResponses(value ={
           @ApiResponse(responseCode = "200", description = "lista de tecnicos obtenida correctamente",
             content = @Content(schema= @Schema(implementation = tecnicos.class))),
           @ApiResponse(responseCode = "204", description = "no hay tecnicos registrados")
     })
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
    @Operation(summary = "Buscar técnico por ID", description = "Devuelve los datos del técnico solicitado")
          @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Técnico encontrado correctamente",
        content = @Content(schema = @Schema(implementation = tecnicos.class))),
    @ApiResponse(responseCode = "404", description = "Técnico no encontrado")
})
    @GetMapping("/tecnicos/{id}")
    public ResponseEntity<?> buscarporid(@PathVariable Long id) {
        try {
            tecnicos tec = serviceTecnicos.buscarporid(id);

             tec.add(linkTo(methodOn(tecnicocontroller.class).buscarporid(tec.getId())).withSelfRel());
             tec.add(linkTo(methodOn(tecnicocontroller.class).actualizarEstado(tec.getId(),null)).withRel("actualizar estado"));
             tec.add(linkTo(methodOn(tecnicocontroller.class).actualizarEspecialidad(tec.getId(),null)).withRel("actualizar especialidad"));

            return ResponseEntity.ok(tec);
    }   catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
    }
    

    //endpoint para crear un nuevo tecnico
    @Operation(summary = "Crear nuevo técnico", description = "Permite registrar un nuevo técnico")
       @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Técnico creado correctamente",
        content = @Content(schema = @Schema(implementation = tecnicos.class))),
    @ApiResponse(responseCode = "409", description = "Tecnico no se puede agregar por que su rut ya esta en uso ")
})
    @PostMapping("/tecnicos")
    public ResponseEntity<?> creartecnico(@RequestBody tecnicos tecnico){
    try {
        tecnicos newtecnico = serviceTecnicos.AgregarTecnico(
            tecnico.getRut(),
            tecnico.getNombre(),
            tecnico.getEspecialidad()
        );
        
         newtecnico.add(linkTo(methodOn(tecnicocontroller.class).actualizarEstado(newtecnico.getId(),null)).withRel("actualizar estado"));
         newtecnico.add(linkTo(methodOn(tecnicocontroller.class).actualizarEspecialidad(newtecnico.getId(),null)).withRel("actualizar especialidad"));
         newtecnico.add(linkTo(methodOn(tecnicocontroller.class).buscarporid(newtecnico.getId())).withRel("buscar por id"));
        return ResponseEntity.status(HttpStatus.CREATED).body(newtecnico);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    }

    // Endpoint para actualizar la especialidad de un técnico
        @Operation(summary = "Actualizar especialidad del técnico", description = "Permite actualizar la especialidad del técnico indicado por su ID")
     @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Especialidad actualizada correctamente",
        content = @Content(schema = @Schema(implementation = tecnicos.class))),
    @ApiResponse(responseCode = "404", description = "Técnico no encontrado, no se puede actualizar la especialidad")
})
    @PutMapping("/tecnicos/{id}/especialidad")
    public ResponseEntity<?> actualizarEspecialidad(@PathVariable Long id, @RequestBody Map<String, String> body) {
    try {
        String especialidad = body.get("especialidad");

       
        tecnicos tecnicoActualizado = serviceTecnicos.actualizarEspecializacion(id, especialidad);

         tecnicoActualizado.add(linkTo(methodOn(tecnicocontroller.class).actualizarEspecialidad(tecnicoActualizado.getId(),null)).withSelfRel());
         tecnicoActualizado.add(linkTo(methodOn(tecnicocontroller.class).actualizarEstado(tecnicoActualizado.getId(),null)).withRel("actualizar estado"));
         tecnicoActualizado.add(linkTo(methodOn(tecnicocontroller.class).buscarporid(tecnicoActualizado.getId())).withRel("buscar por id"));

       
        return ResponseEntity.ok(tecnicoActualizado);

    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
//endpoint para actualizar estado activo/no activo
 @Operation(summary = "Actualizar estado del técnico", description = "Permite actualizar el estado del técnico indicado por su ID")
     @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
        content = @Content(schema = @Schema(implementation = tecnicos.class))),
    @ApiResponse(responseCode = "404", description = "Técnico no encontrado, no se puede actualizar el estado")
})
   @PutMapping("/tecnicos/{id}/estado")
public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, Object> body) {
    try {
      
        boolean estado = Boolean.parseBoolean(body.get("estado").toString());

        tecnicos tecnicoActualizado = serviceTecnicos.actualizarestado(id, estado);

         tecnicoActualizado.add(linkTo(methodOn(tecnicocontroller.class).actualizarEstado(tecnicoActualizado.getId(),null)).withSelfRel());
         tecnicoActualizado.add(linkTo(methodOn(tecnicocontroller.class).actualizarEspecialidad(tecnicoActualizado.getId(),null)).withRel("actualizar especialidad"));
         tecnicoActualizado.add(linkTo(methodOn(tecnicocontroller.class).buscarporid(tecnicoActualizado.getId())).withRel("buscar por id"));

        return ResponseEntity.ok(tecnicoActualizado);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    
}
}
}
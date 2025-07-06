package com.example.equipos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.equipos.model.equipo;
import com.example.equipos.service.EquipoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;




@Tag(name = "Equipos", description = "operaciones relacionadas con la gestión de equipos")
@RestController
@RequestMapping("/api/v1")
public class EquipoController {
     @Autowired
     private EquipoService equiposervice;

    // endpoint para obtener todos los equipos
    @Operation(summary = "obtener todos los equipos", description = "devuelve una lista con todos los equipos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de equipos obtenida correctamente",
            content = @Content(schema = @Schema(implementation = equipo.class))),
        @ApiResponse(responseCode = "204", description = "No hay equipos registrados")
    })
    @GetMapping("/equipos")
    public ResponseEntity<List<equipo>> obtenertodoslosquipos() {
        List<equipo> Equipo = equiposervice.buscarquipos();
        //si la lista esta vacia
        if(Equipo.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Equipo);
    }

    // endpoint para agregar un equipo
    @Operation(summary = "Agregar/registrar un nuevo equipo", description = "registra un nuevo equipo asociado a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Equipo creado correctamente",
            content = @Content(schema = @Schema(implementation = equipo.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado, no se puede agregar el equipo")
    })
    @PostMapping("/equipos")
    public ResponseEntity<?> agregarequipo(@RequestBody equipo newequipo) {
        try {

        equipo equipoagregado = equiposervice.agregarequipo(
            newequipo.getIdusuario(), 
            newequipo.getTipodispositivo(),
            newequipo.getMarca(),
            newequipo.getModelo()
            );

            equipoagregado.add(linkTo(methodOn(EquipoController.class).obtenerequiporid(equipoagregado.getId())).withRel("buscar equipo por id"));
            equipoagregado.add(linkTo(methodOn(EquipoController.class).eliminarquipoporid(equipoagregado.getId())).withRel("eliminar equipo por id"));

        return ResponseEntity.status(HttpStatus.CREATED).body(equipoagregado);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }

     // endpoint para eliminar un equipo
    @Operation(summary = "Eliminar un equipo", description = "Elimina un equipo registrado segun su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipo eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @DeleteMapping("/equipos/{id}")
    public ResponseEntity<String> eliminarquipoporid(@Parameter(description = "id equipo", required =true)@PathVariable Long id){
        try {
        String mensaje = equiposervice.eliminarequipoporid(id);
        
        return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

     // endpoint para buscar todos los equipos de un usuario
    @Operation(summary = "Obtener equipos por usuario", description = "Devuelve todos los equipos registrados con  un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipos encontrados",
            content = @Content(schema = @Schema(implementation = equipo.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron equipos para el usuario indicado o el usuario no existe")
    })
    @GetMapping("/equipos/todos/{idusuario}")
    public ResponseEntity<?> buscarequiposdeusuarioporid(@Parameter(description = "id usuario", required =true)@PathVariable Long idusuario) {
        try {
         List<equipo> equipos = equiposervice.buscarporidusuario(idusuario);

        return ResponseEntity.ok(equipos);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    // endpoint para obtener equipo por ID
    @Operation(summary = "Obtener equipo por su ID", description = "devuelve un equipo especifico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipo encontrado",
            content = @Content(schema = @Schema(implementation = equipo.class))),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
     @GetMapping("/equipos/{id}")
    public ResponseEntity<?> obtenerequiporid(@Parameter(description = "id equipo", required =true)@PathVariable Long id) {
           try {
            equipo equipo = equiposervice.buscarporid(id);

            equipo.add(linkTo(methodOn(EquipoController.class).obtenerequiporid(equipo.getId())).withSelfRel());
            equipo.add(linkTo(methodOn(EquipoController.class).eliminarquipoporid(equipo.getId())).withRel("eliminar"));
            return ResponseEntity.ok(equipo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
 
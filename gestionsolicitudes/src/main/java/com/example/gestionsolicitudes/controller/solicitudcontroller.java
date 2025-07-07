package com.example.gestionsolicitudes.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.gestionsolicitudes.dto.actualizarsolicitud;
import com.example.gestionsolicitudes.model.solicitud;

import com.example.gestionsolicitudes.services.solicitudservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PutMapping;


@Tag(name = "solicitudes", description = "operaciones relacionadas con la gestión de solicitudes")
@RestController
@RequestMapping("api/v1")
public class solicitudcontroller {
    @Autowired
    private solicitudservice solicitudservice;


 //endponit para consultar todas las solicitudes
   @Operation(summary = "obtener todas las solicitudes", description = "devuelve una lista con todas las solicitudes registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente",
            content = @Content(schema = @Schema(implementation = solicitud.class))),
        @ApiResponse(responseCode = "204", description = "No hay solicitudes registrados")
    })
    @GetMapping("/solicitudes")
    public ResponseEntity<List<solicitud>> obtenersolcitudes(){
      List<solicitud> solicitud = solicitudservice.buscarsolicitudes();
        //si la lista esta vacia
        if(solicitud.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(solicitud);
    }
    //endpoint para buscar solicitud por id
    @Operation(summary = "Obtener solicitud por id", description = "retorna la solicitud solicitada.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "solcitud obtenida correctamente",
        content = @Content(schema = @Schema(implementation = solicitud.class))),
    @ApiResponse(responseCode = "404", description = "solicitud no encontrada/inexistente")
})
    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<?> buscarporid(@Parameter(description = "id de la solicitud",required = true)@PathVariable Long id) {
        try {
         solicitud solicitud = solicitudservice.buscarporid(id);

        solicitud.add(linkTo(methodOn(solicitudcontroller.class).buscarporid(solicitud.getId())).withSelfRel());
        solicitud.add(linkTo(methodOn(solicitudcontroller.class).eliminarSolicitud(solicitud.getId())).withRel("eliminar solicitud por id"));
        solicitud.add(linkTo(methodOn(solicitudcontroller.class).buscartodasporusuario(solicitud.getIdusuario())).withRel("buscar todas las solicitudes de un usuario"));
        solicitud.add(linkTo(methodOn(solicitudcontroller.class).actualizarporusuario(solicitud.getIdusuario(),solicitud.getId(),null)).withRel("actualizar solicitud por id usuario y id solicitud"));

        return ResponseEntity.ok(solicitud);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    

    //endpoint para ver todas las solicitudes de un usuario
@Operation(summary = "Obtener solicitudes por usuario", description = "retorna una lista con todas las solicitudes registradas de un usuario específico.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes del usuario obtenida correctamente",
        content = @Content(schema = @Schema(implementation = solicitud.class))),
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado o sin solicitudes")
})
    @GetMapping("/solicitudes/usuario/{idusuario}")
    public ResponseEntity<?> buscartodasporusuario(@Parameter(description = "llave foranea de id usuario",required = true)@PathVariable Long idusuario) {
        try {
         List<solicitud> solicitudes = solicitudservice.buscarporidusuario(idusuario);
         

        return ResponseEntity.ok(solicitudes);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    // Endpoint para crear una nueva solicitud
@Operation(summary = "Crear nueva solicitud", description = "Registra una nueva solicitud con los datos.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente",
        content = @Content(schema = @Schema(implementation = solicitud.class))),
    @ApiResponse(responseCode = "404", description = "Error al crear la solicitud, usuario no encontrado o datos incorrectos")
})
    @PostMapping("/solicitudes")
    public ResponseEntity<?> crearsolicitud(@RequestBody solicitud solicitud){
    try {
        solicitud newsolicitud = solicitudservice.crearsolicitud(
            solicitud.getTiposolicitud(),
            solicitud.getDescripciongeneral(),
            solicitud.getIdusuario(),
            solicitud.getIdequipo()
        );
        newsolicitud.add(linkTo(methodOn(solicitudcontroller.class).crearsolicitud(newsolicitud)).withSelfRel());
        newsolicitud.add(linkTo(methodOn(solicitudcontroller.class).buscarporid(newsolicitud.getId())).withRel("buscar solicitud por id"));
        newsolicitud.add(linkTo(methodOn(solicitudcontroller.class).eliminarSolicitud(newsolicitud.getId())).withRel("eliminar solicitud por id"));
        newsolicitud.add(linkTo(methodOn(solicitudcontroller.class).buscartodasporusuario(newsolicitud.getIdusuario())).withRel("busacr todas las solicitudes de un usuario"));
        newsolicitud.add(linkTo(methodOn(solicitudcontroller.class).actualizarporusuario(newsolicitud.getIdusuario(),newsolicitud.getId(),null)).withRel("actualizar solicitud por id"));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsolicitud);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }
    //endpoint para actualizar una solicitud por usuario
    @Operation(summary = "Actualizar solicitud por usuario", description = "Actualiza una solicitud existente para un usuario específico.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Solicitud actualizada correctamente",
        content = @Content(schema = @Schema(implementation = solicitud.class))),
    @ApiResponse(responseCode = "400", description = "Solicitud no válida o error en la actualización")
})
    @PutMapping("/solicitudes/usuario/{idusuario}/{idsolicitud}")
    public ResponseEntity<?> actualizarporusuario(@Parameter(description = "llave foranea de id usuario",required = true)@PathVariable Long idusuario, @Parameter(description = "id de solicitud ya existente",required = true)@PathVariable Long idsolicitud,@RequestBody actualizarsolicitud datosActualizados) {
    try {
        solicitud solicitudActualizada = solicitudservice.actualizarporusuario(
            idsolicitud,
            idusuario,
            datosActualizados.getTiposolicitud(),
            datosActualizados.getDescripciongeneral()
        );

        return ResponseEntity.ok(solicitudActualizada);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    
}
//eliminar solicitud por id 
@Operation(summary = "Eliminar solicitud", description = "Elimina una solicitud específica por su ID.")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Solicitud eliminada correctamente"),
    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada ID incorrecto/invalido")
})
@DeleteMapping("/solicitudes/{idsolicitud}")
public ResponseEntity<?> eliminarSolicitud(@Parameter(description = "ID de la solicitud a eliminar" , required = true)@PathVariable Long idsolicitud){
 try {
        
        String mensaje = solicitudservice.eliminarporid(idsolicitud);

        EntityModel<String> newmensaje = EntityModel.of(mensaje);
        newmensaje.add(linkTo(methodOn(solicitudcontroller.class).crearsolicitud(null))
            .withRel("crear-solicitud"));

        return ResponseEntity.ok(newmensaje);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

}

}

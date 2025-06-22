package com.example.gestionsolicitudes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.gestionsolicitudes.dto.actualizarsolicitud;
import com.example.gestionsolicitudes.model.solicitud;

import com.example.gestionsolicitudes.services.solicitudservice;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("api/v1")
public class solicitudcontroller {
    @Autowired
    private solicitudservice solicitudservice;
 //endponit para consultar todas las solicitudes
    @GetMapping("/solicitudes")
    public ResponseEntity<List<solicitud>> obtenersolcitudes(){
      List<solicitud> solicitud = solicitudservice.buscarsolicitudes();
        //si la lista esta vacia
        if(solicitud.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(solicitud);
    }
    //endpoint para ver todas las solicitudes de un usuario
    @GetMapping("/solicitudes/{idusuario}")
    public ResponseEntity<?> buscartodasporusuario(@PathVariable Long idusuario) {
        try {
         List<solicitud> solicitudes = solicitudservice.buscarporidusuario(idusuario);

        return ResponseEntity.ok(solicitudes);
        } catch (RuntimeException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    //endpoint para crea un nuevo usuario
    @PostMapping("/solicitudes")
    public ResponseEntity<?> crearsolicitud(@RequestBody solicitud solicitud){
    try {
        solicitud newsolicitud = solicitudservice.crearsolicitud(
            solicitud.getTiposolicitud(),
            solicitud.getDescripciongeneral(),
            solicitud.getIdusuario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newsolicitud);
    }   catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }
    //endpoint para actualizar una solicitud por usuario
    @PutMapping("/solicitudes/{idusuario}/{idsolicitud}")
    public ResponseEntity<?> actualizarporusuario(@PathVariable Long idusuario, @PathVariable Long idsolicitud,@RequestBody actualizarsolicitud datosActualizados) {
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


}

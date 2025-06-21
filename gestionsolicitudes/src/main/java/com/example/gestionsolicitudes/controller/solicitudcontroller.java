package com.example.gestionsolicitudes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.example.gestionsolicitudes.model.solicitud;

import com.example.gestionsolicitudes.services.solicitudservice;

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


}

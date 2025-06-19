package com.example.gestionsolicitudes.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestionsolicitudes.model.solicitud;
import com.example.gestionsolicitudes.repository.solicitudrepository;
import com.example.gestionsolicitudes.webclient.usuarioclient;

@Service
public class solicitudservice {
@Autowired
private solicitudrepository Solicitudrepository;
 @Autowired
    private usuarioclient usuarioClient;

//metodo para buscar todas las solicitudes
public List<solicitud> buscarsolicitudes(){
    return Solicitudrepository.findAll();
}

//metodo para crear una nueva solicitud
public solicitud crearsolicitud(String tiposolicitud, String descripciongeneral, long idusuario){
     // Validar si el usuario existe antes de crear el reporte
        Map<String, Object> usuario = usuarioClient.obtenerusuarioid(idusuario);

        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado, no se puede agregar la solicitud");
        }
    solicitud solicitud = new solicitud();
    solicitud.setTiposolicitud(tiposolicitud);
    solicitud.setDescripciongeneral(descripciongeneral);
    solicitud.setIdusuario(idusuario);

    return Solicitudrepository.save(solicitud);
}

}
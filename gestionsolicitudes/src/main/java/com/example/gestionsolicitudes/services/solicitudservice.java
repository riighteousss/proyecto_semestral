package com.example.gestionsolicitudes.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestionsolicitudes.model.solicitud;
import com.example.gestionsolicitudes.repository.solicitudrepository;
import com.example.gestionsolicitudes.webclient.equipoclient;
import com.example.gestionsolicitudes.webclient.usuarioclient;

@Service
public class solicitudservice {
@Autowired
private solicitudrepository Solicitudrepository;
 @Autowired
    private usuarioclient usuarioClient;
 @Autowired
    private equipoclient equipoClient;

//metodo para buscar todas las solicitudes
public List<solicitud> buscarsolicitudes(){
    return Solicitudrepository.findAll();
}
//buscar solicitudes por id
public solicitud buscarporid(Long id){
  return Solicitudrepository.findById(id)
 .orElseThrow(()-> new RuntimeException("Solicitud no encontrada. porfavor escriba un id valido."));
}
//metodo para buscar todos las solicitudes de un usuario
public List<solicitud>buscarporidusuario(Long idusuario){
    List<solicitud> solicitud = Solicitudrepository.findByIdusuario(idusuario);
        if (solicitud.isEmpty()) {
              throw new RuntimeException("No se encontraron solicitudes de usuario con ID: " + idusuario);
           }
          return solicitud;
  
}
//metodo para crear una nueva solicitud
public solicitud crearsolicitud(String tiposolicitud, String descripciongeneral, long idusuario,Long idequipo){
     // Validar si el usuario existe antes de crear el reporte
        Map<String, Object> usuario = usuarioClient.obtenerusuarioid(idusuario);

        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado, no se puede agregar la solicitud");
        }


     //valida si existe el equipo resgistrado

      Map<String, Object> equipo = equipoClient.obtenerequipoid(idequipo);


        if (equipo == null || equipo.isEmpty()) {
            throw new RuntimeException("equipo no encontrado, no se puede agregar la solicitud");
        }
    solicitud solicitud = new solicitud();
    solicitud.setTiposolicitud(tiposolicitud);
    solicitud.setDescripciongeneral(descripciongeneral);
    solicitud.setIdusuario(idusuario);
    solicitud.setIdequipo(idequipo);

    return Solicitudrepository.save(solicitud);
}
//metodo para eliminar una solicitud 
public String eliminarsolicitudporid(Long id){
       if(!Solicitudrepository.existsById(id)){
            throw new RuntimeException("la solicitud de ID:"+id +" no existe");
       }
      Solicitudrepository.deleteById(id);

      return "La solicitud de ID:"+id+" se ah eliminado exitosamente";
}
//metodo para actualizar una solicitud especifica de un usuario 
public solicitud actualizarporusuario(Long idsolicitud, Long idusuario, String nuevoTipoSolicitud, String nuevaDescripcionGeneral) {
    
    // valida si la solicitud existe
    solicitud solicitudExistente = Solicitudrepository.findById(idsolicitud)
        .orElseThrow(() -> new RuntimeException("Solicitud con ID: " + idsolicitud + " no encontrada"));

    // Validar que la solicitud le pertenezca al usuario
    if (!solicitudExistente.getIdusuario().equals(idusuario)) {
        throw new RuntimeException("La solicitud no pertenece al usuario con ID: " + idusuario);
    }

    // Actualiza los datos que se  quieran cambiar
    solicitudExistente.setTiposolicitud(nuevoTipoSolicitud);
    solicitudExistente.setDescripciongeneral(nuevaDescripcionGeneral);

    return Solicitudrepository.save(solicitudExistente);
}
//metodo para eliminar una solicitud
public String eliminarporid(Long id){
    if(!Solicitudrepository.existsById(id)){
        throw new RuntimeException("la solicitud de ID: "+id+" No existe");
    }
    Solicitudrepository.deleteById(id);

    return "la solicitud de ID:"+id+" se ah eliminado exitosamente";

}

}
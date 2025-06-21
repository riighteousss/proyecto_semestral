package com.example.gestionsolicitudes.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // Método para buscar todas las solicitudes
    public List<solicitud> buscarsolicitudes(){
        return Solicitudrepository.findAll();
    }

    // Método para buscar solicitudes por usuario específico
    public List<solicitud> buscarSolicitudesPorUsuario(Long idUsuario) {
        // Validar si el usuario existe
        Map<String, Object> usuario = usuarioClient.obtenerusuarioid(idUsuario);
        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return Solicitudrepository.findByIdusuario(idUsuario);
    }

    // Método para buscar una solicitud por ID
    public solicitud buscarSolicitudPorId(Long id) {
        Optional<solicitud> solicitudOpt = Solicitudrepository.findById(id);
        if (solicitudOpt.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + id);
        }
        return solicitudOpt.get();
    }

    // Método para crear una nueva solicitud
    public solicitud crearsolicitud(String tiposolicitud, String descripciongeneral, long idusuario){
        // Validar si el usuario existe antes de crear la solicitud
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

    // Método para modificar una solicitud existente
    public solicitud modificarSolicitud(Long id, String tiposolicitud, String descripciongeneral) {
        Optional<solicitud> solicitudOpt = Solicitudrepository.findById(id);
        
        if (solicitudOpt.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + id);
        }
        
        solicitud solicitudExistente = solicitudOpt.get();
        
        // Actualizar solo los campos que no sean nulos
        if (tiposolicitud != null && !tiposolicitud.trim().isEmpty()) {
            solicitudExistente.setTiposolicitud(tiposolicitud);
        }
        if (descripciongeneral != null && !descripciongeneral.trim().isEmpty()) {
            solicitudExistente.setDescripciongeneral(descripciongeneral);
        }
        
        return Solicitudrepository.save(solicitudExistente);
    }

    // Método para eliminar una solicitud
    public void eliminarSolicitud(Long id) {
        Optional<solicitud> solicitudOpt = Solicitudrepository.findById(id);
        
        if (solicitudOpt.isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada con ID: " + id);
        }
        
        Solicitudrepository.deleteById(id);
    }
}
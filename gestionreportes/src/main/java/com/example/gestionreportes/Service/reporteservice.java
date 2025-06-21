package com.example.gestionreportes.Service;

import java.util.List;
import java.util.Map;

import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Repository.reporterepository;
import com.example.gestionreportes.webclient.usuarioclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class reporteservice {
  
    @Autowired
    private reporterepository reporteRepository;

    @Autowired
    private usuarioclient usuarioClient;

    // Buscar todos los reportes
    public List<reporte> buscarReportes() {
        return reporteRepository.findAll();
    
    }

    //buscar reporte por id
    public reporte buscarreporteporid(Long id){
        return reporteRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("reporte no encontrado"));
    }

    // Crear un nuevo reporte
    public reporte crearReporte(String tipoReporte, String descripcionGeneral, Long idUsuario) {
        // Validar si el usuario existe antes de crear el reporte
        Map<String, Object> usuario = usuarioClient.obtenerusuarioid(idUsuario);

        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado, no se puede agregar el reporte");
        }

        // Crear el objeto Reporte
        reporte nuevoReporte = new reporte();
        nuevoReporte.setTiporeporte(tipoReporte);
        nuevoReporte.setDescripciongeneral(descripcionGeneral);
        nuevoReporte.setIdusuario(idUsuario);

        return reporteRepository.save(nuevoReporte);
    }
    //eliminar reporte
    public String eliminarreporte(Long id){
        //valida que exista antes de borrar
        
        if(!reporteRepository.existsById(id)){          
            throw new RuntimeException("El reporte con ID: "+id+" no existe");
        }
        reporteRepository.deleteById(id);

        return "El reporte se ha eliminado exitosamente";
    }
    //buscar todos los reportes de un usuario por su id 
     public List<reporte> buscarporidusuario(Long idUsuario) {
          List<reporte> newreport = reporteRepository.findByIdusuario(idUsuario);
        if (newreport.isEmpty()) {
              throw new RuntimeException("No se encontraron equipos para el usuario con ID: " + idUsuario);
           }
          return newreport;
    }

}

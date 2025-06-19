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

}

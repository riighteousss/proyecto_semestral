package com.example.gestionreportes.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // Buscar reporte por ID
    public Optional<reporte> buscarPorId(Long id) {
        return reporteRepository.findById(id);
    }

    // Buscar reportes por usuario
    public List<reporte> buscarPorUsuario(Long idUsuario) {
        return reporteRepository.findByIdusuario(idUsuario);
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

    // Actualizar reporte
    public reporte actualizarReporte(Long id, String tipoReporte, String descripcionGeneral, Long idUsuario) {
        Optional<reporte> reporteExistente = reporteRepository.findById(id);
        
        if (!reporteExistente.isPresent()) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }

        // Validar si el usuario existe
        if (idUsuario != null) {
            Map<String, Object> usuario = usuarioClient.obtenerusuarioid(idUsuario);
            if (usuario == null || usuario.isEmpty()) {
                throw new RuntimeException("Usuario no encontrado, no se puede actualizar el reporte");
            }
        }

        reporte reporte = reporteExistente.get();
        if (tipoReporte != null) reporte.setTiporeporte(tipoReporte);
        if (descripcionGeneral != null) reporte.setDescripciongeneral(descripcionGeneral);
        if (idUsuario != null) reporte.setIdusuario(idUsuario);

        return reporteRepository.save(reporte);
    }

    // Eliminar reporte
    public void eliminarReporte(Long id) {
        Optional<reporte> reporteExistente = reporteRepository.findById(id);
        
        if (!reporteExistente.isPresent()) {
            throw new RuntimeException("Reporte no encontrado con ID: " + id);
        }

        reporteRepository.deleteById(id);
    }
}

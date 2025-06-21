package com.example.gestionhistorial.service;

import com.example.gestionhistorial.model.HistorialReparacion;
import com.example.gestionhistorial.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistorialService {
    @Autowired
    private HistorialRepository historialRepository;

    public List<HistorialReparacion> buscarTodos() {
        return historialRepository.findAll();
    }

    public HistorialReparacion crearRegistro(Long solicitudId, String accion, String usuario) {
        return crearRegistro(solicitudId, accion, usuario, null);
    }

    public HistorialReparacion crearRegistro(Long solicitudId, String accion, String usuario, Long tecnicoId) {
        // Validación 1: Campos obligatorios
        if (solicitudId == null) {
            throw new RuntimeException("El ID de solicitud es requerido");
        }
        
        if (accion == null || accion.trim().isEmpty()) {
            throw new RuntimeException("La acción no puede estar vacía");
        }
        
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new RuntimeException("El usuario es requerido");
        }

        // Validación 2: ID positivo
        if (solicitudId <= 0) {
            throw new RuntimeException("El ID de solicitud debe ser positivo");
        }

        // Validación 3: Longitud máxima para acción (100 caracteres)
        if (accion.length() > 100) {
            throw new RuntimeException("La acción no puede exceder 100 caracteres");
        }

        // Validación 4: TecnicoId si se proporciona
        if (tecnicoId != null && tecnicoId <= 0) {
            throw new RuntimeException("El ID del técnico debe ser positivo");
        }

        HistorialReparacion registro = new HistorialReparacion();
        registro.setSolicitudId(solicitudId);
        registro.setAccion(accion);
        registro.setUsuario(usuario);
        registro.setTecnicoId(tecnicoId);
        return historialRepository.save(registro);
    }

    // Método simple para buscar por solicitud
    public List<HistorialReparacion> buscarPorSolicitud(Long solicitudId) {
        if (solicitudId == null || solicitudId <= 0) {
            throw new RuntimeException("ID de solicitud inválido");
        }
        return historialRepository.findBySolicitudId(solicitudId);
    }

    // Nuevo método: Filtrar por usuario
    public List<HistorialReparacion> buscarPorUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new RuntimeException("El usuario es requerido para la búsqueda");
        }
        return historialRepository.findByUsuario(usuario.trim());
    }

    // Nuevo método: Filtrar por técnico
    public List<HistorialReparacion> buscarPorTecnico(Long tecnicoId) {
        if (tecnicoId == null || tecnicoId <= 0) {
            throw new RuntimeException("ID de técnico inválido");
        }
        return historialRepository.findByTecnicoId(tecnicoId);
    }

    // Nuevo método: Filtrar por solicitud y usuario
    public List<HistorialReparacion> buscarPorSolicitudYUsuario(Long solicitudId, String usuario) {
        if (solicitudId == null || solicitudId <= 0) {
            throw new RuntimeException("ID de solicitud inválido");
        }
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new RuntimeException("El usuario es requerido para la búsqueda");
        }
        return historialRepository.findBySolicitudIdAndUsuario(solicitudId, usuario.trim());
    }

    // Nuevo método: Filtrar por solicitud y técnico
    public List<HistorialReparacion> buscarPorSolicitudYTecnico(Long solicitudId, Long tecnicoId) {
        if (solicitudId == null || solicitudId <= 0) {
            throw new RuntimeException("ID de solicitud inválido");
        }
        if (tecnicoId == null || tecnicoId <= 0) {
            throw new RuntimeException("ID de técnico inválido");
        }
        return historialRepository.findBySolicitudIdAndTecnicoId(solicitudId, tecnicoId);
    }

    // Nuevo método: Buscar por usuario con filtro de acción
    public List<HistorialReparacion> buscarPorUsuarioYAccion(String usuario, String accionFiltro) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new RuntimeException("El usuario es requerido para la búsqueda");
        }
        if (accionFiltro == null || accionFiltro.trim().isEmpty()) {
            throw new RuntimeException("El filtro de acción es requerido");
        }
        return historialRepository.findByUsuarioAndAccionContaining(usuario.trim(), accionFiltro.trim());
    }

    // Nuevo método: Buscar por técnico con filtro de acción
    public List<HistorialReparacion> buscarPorTecnicoYAccion(Long tecnicoId, String accionFiltro) {
        if (tecnicoId == null || tecnicoId <= 0) {
            throw new RuntimeException("ID de técnico inválido");
        }
        if (accionFiltro == null || accionFiltro.trim().isEmpty()) {
            throw new RuntimeException("El filtro de acción es requerido");
        }
        return historialRepository.findByTecnicoIdAndAccionContaining(tecnicoId, accionFiltro.trim());
    }
}
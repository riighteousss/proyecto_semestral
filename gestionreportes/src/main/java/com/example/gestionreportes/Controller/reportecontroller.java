package com.example.gestionreportes.Controller;

import com.example.gestionreportes.Model.reporte;
import com.example.gestionreportes.Service.reporteservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class reportecontroller {
    
    @Autowired
    private reporteservice reporteService;

    // Obtener todos los reportes
    @GetMapping("/reportes")
    public ResponseEntity<List<reporte>> obtenerReportes() {
        List<reporte> reportes = reporteService.buscarReportes();
        return reportes.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(reportes);
    }

    // Obtener reporte por ID
    @GetMapping("/reportes/{id}")
    public ResponseEntity<?> obtenerReportePorId(@PathVariable Long id) {
        try {
            Optional<reporte> reporte = reporteService.buscarPorId(id);
            return reporte.isPresent() ? 
                ResponseEntity.ok(reporte.get()) : 
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Obtener reportes por usuario - NUEVA FUNCIONALIDAD
    @GetMapping("/reportes/usuario/{usuarioId}")
    public ResponseEntity<List<reporte>> obtenerReportesPorUsuario(@PathVariable Long usuarioId) {
        List<reporte> reportes = reporteService.buscarPorUsuario(usuarioId);
        return reportes.isEmpty() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.ok(reportes);
    }

    // Crear nuevo reporte
    @PostMapping("/reportes")
    public ResponseEntity<?> crearReporte(@RequestBody reporte reporte) {
        try {
            reporte nuevoReporte = reporteService.crearReporte(
                reporte.getTiporeporte(),
                reporte.getDescripciongeneral(),
                reporte.getIdusuario()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoReporte);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Actualizar reporte completo
    @PutMapping("/reportes/{id}")
    public ResponseEntity<?> actualizarReporte(
            @PathVariable Long id,
            @RequestBody reporte reporteData) {
        try {
            reporte reporteActualizado = reporteService.actualizarReporte(
                id,
                reporteData.getTiporeporte(),
                reporteData.getDescripciongeneral(),
                reporteData.getIdusuario()
            );
            return ResponseEntity.ok(reporteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Eliminar reporte
    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        try {
            reporteService.eliminarReporte(id);
            return ResponseEntity.ok().body("Reporte eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
